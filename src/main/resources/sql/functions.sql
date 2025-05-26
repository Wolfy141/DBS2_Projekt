CREATE VIEW available_equipment_view AS
SELECT e.id, e.name, e.category, e.price_per_day
FROM equipment e
WHERE e.id NOT IN (
    SELECT r.equipment_id
    FROM rentals r
    WHERE r.return_date IS NULL
);

CREATE VIEW customer_rentals_view AS
SELECT r.id, e.name, r.start_date, r.expected_return_date
FROM rentals r
         JOIN equipment e ON r.equipment_id = e.id
WHERE r.return_date IS NULL;

CREATE VIEW equipment_stats_view AS
SELECT
    e.id,
    e.name,
    COUNT(r.id) AS rental_count,
    AVG(r.duration_days) AS avg_rental_days
FROM equipment e
         LEFT JOIN rentals r ON e.id = r.equipment_id
GROUP BY e.id, e.name;

CREATE FUNCTION calculate_rental_cost(
    p_rental_id INT,
    p_actual_return_date DATE
) RETURNS DECIMAL(10,2)
BEGIN
    DECLARE v_daily_rate DECIMAL(10,2);
    DECLARE v_expected_days INT;
    DECLARE v_actual_days INT;
    DECLARE v_penalty DECIMAL(10,2) DEFAULT 0;

    SELECT
        e.price_per_day,
        DATEDIFF(r.expected_return_date, r.start_date)
    INTO v_daily_rate, v_expected_days
    FROM rentals r
    JOIN equipment e ON r.equipment_id = e.id
    WHERE r.id = p_rental_id;

    SET v_actual_days = DATEDIFF(p_actual_return_date, (SELECT start_date FROM rentals WHERE id = p_rental_id));

    -- Pokuta 20% za každý den prodlení
    IF v_actual_days > v_expected_days THEN
    SET v_penalty = (v_actual_days - v_expected_days) * v_daily_rate * 0.2;
    END IF;

    RETURN (v_actual_days * v_daily_rate) + v_penalty;
    END;

    public boolean checkEquipmentAvailability(int equipmentId, LocalDate startDate, LocalDate endDate) {
    String sql = "SELECT COUNT(*) FROM rentals WHERE equipment_id = ? AND " +
                 "((start_date BETWEEN ? AND ?) OR " +
                 "(expected_return_date BETWEEN ? AND ?)) AND return_date IS NULL";

    int count = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            equipmentId, startDate, endDate, startDate, endDate
                );
    return count == 0;
    }

@Scheduled(cron = "0 0 9 * * ?") // Každý den v 9:00
public void sendReturnReminders() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    List<Rental> expiringRentals = jdbcTemplate.query(
        "SELECT * FROM rentals WHERE expected_return_date = ? AND return_date IS NULL",
        new BeanPropertyRowMapper<>(Rental.class),
        tomorrow
    );

    expiringRentals.forEach(rental -> {
        emailService.sendEmail(
            rental.getCustomerEmail(),
            "Připomínka vrácení vybavení",
            "Vážený zákazníku, doneste prosím vybavení " + rental.getEquipmentName() + " do zítřejšího dne."
        );
    });
}

CREATE PROCEDURE create_rental(
    IN p_customer_id INT,
    IN p_equipment_id INT,
    IN p_days INT,
    OUT p_status VARCHAR(100)
BEGIN
    DECLARE v_available BOOLEAN;

    -- Kontrola dostupnosti
    SELECT COUNT(*) = 0 INTO v_available
    FROM rentals
    WHERE equipment_id = p_equipment_id
    AND return_date IS NULL;

    IF v_available THEN
        INSERT INTO rentals (
            customer_id,
            equipment_id,
            start_date,
            expected_return_date
        ) VALUES (
            p_customer_id,
            p_equipment_id,
            CURDATE(),
            DATE_ADD(CURDATE(), INTERVAL p_days DAY)
        );
        SET p_status = 'OK';
    ELSE
        SET p_status = 'EQUIPMENT_NOT_AVAILABLE';
    END IF;
END;

CREATE PROCEDURE return_equipment(
    IN p_rental_id INT,
    IN p_condition VARCHAR(20),
    OUT p_total_cost DECIMAL(10,2))
BEGIN
    DECLARE v_damage_fee DECIMAL(10,2) DEFAULT 0;

    -- Pokuta za poškození
    IF p_condition = 'DAMAGED' THEN
        SET v_damage_fee = 1000; -- Fixní pokuta 1000 Kč
    END IF;

    -- Výpočet ceny
    SET p_total_cost = calculate_rental_cost(p_rental_id, CURDATE()) + v_damage_fee;

    -- Aktualizace záznamu
    UPDATE rentals
    SET
        return_date = CURDATE(),
        condition = p_condition,
        total_cost = p_total_cost
    WHERE id = p_rental_id;
END;

CREATE PROCEDURE generate_monthly_report(IN p_month INT, IN p_year INT)
BEGIN
    SELECT
        e.name,
        COUNT(r.id) AS rental_count,
        SUM(r.total_cost) AS total_income
    FROM equipment e
    LEFT JOIN rentals r ON e.id = r.equipment_id
        AND MONTH(r.start_date) = p_month
        AND YEAR(r.start_date) = p_year
    GROUP BY e.id;
END;

@Service
public class RentalService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void processRentalWithPayment(int rentalId, PaymentDetails payment) {
        try {
            // 1. Aktualizace stavu výpůjčky
            jdbcTemplate.update(
                "UPDATE rentals SET status = 'PAID' WHERE id = ?",
                rentalId
            );

            // 2. Záznam platby
            jdbcTemplate.update(
                "INSERT INTO payments (rental_id, amount, payment_date) VALUES (?, ?, NOW())",
                rentalId, payment.getAmount()
            );

            // 3. Simulace chyby (např. neplatná karta)
            if (payment.getCardNumber().equals("4111111111111111")) {
                throw new RuntimeException("Platba byla zamítnuta");
            }

        } catch (Exception e) {
            // Rollback se provede automaticky díky @Transactional
            throw new RuntimeException("Transakce selhala: " + e.getMessage());
        }
    }
}