package com.example.bankmangament.Models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataBaseDriverTest {
    @TempDir
    Path tempDir;

    @Test
    void completesCoreBankingOperationsAtomically() {
        String url = "jdbc:sqlite:" + tempDir.resolve("test.db");
        try (DataBaseDriver database = new DataBaseDriver(url)) {
            assertTrue(database.createClientWithAccounts(
                    "Alice", "Rahman", "@arahman1", "Secret1!",
                    1_000, 1_000, "3201 000001", "3201 000002"
            ).success());
            assertTrue(database.createClientWithAccounts(
                    "Bob", "Karim", "@bkarim2", "Secret2!",
                    500, 500, "3201 000003", "3201 000004"
            ).success());

            assertTrue(database.authenticateClient("@arahman1", "Secret1!"));
            assertFalse(database.authenticateClient("@arahman1", "wrong"));

            OperationResult transfer = database.transferSavings("@arahman1", "@bkarim2", 200, "Test");
            assertTrue(transfer.success(), transfer.message());
            assertEquals(800, database.getSavingsAccount("@arahman1").balanceProperty().get(), 0.001);
            assertEquals(700, database.getSavingsAccount("@bkarim2").balanceProperty().get(), 0.001);
            assertEquals(1, database.getTransactions("@arahman1", -1).size());

            OperationResult internal = database.transferBetweenAccounts("@arahman1", true, 100);
            assertTrue(internal.success(), internal.message());
            assertEquals(900, database.getCheckingAccount("@arahman1").balanceProperty().get(), 0.001);
            assertEquals(900, database.getSavingsAccount("@arahman1").balanceProperty().get(), 0.001);

            assertTrue(database.depositSavings("@bkarim2", 50).success());
            assertEquals(750, database.getSavingsAccount("@bkarim2").balanceProperty().get(), 0.001);

            assertTrue(database.deleteClient("@bkarim2").success());
            assertTrue(database.findClient("@bkarim2").isEmpty());
        }
    }
}
