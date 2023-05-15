package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseExample {
	public static void main(String[] args) {
		// sostituisci con l'URL del tuo database
		String url = "jdbc:sqlite:database.db";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {

			String sql = "CREATE TABLE clienti (\n" + "    numerocliente INTEGER PRIMARY KEY,\n"
					+ "    nome TEXT NOT NULL,\n" + "    cognome TEXT NOT NULL,\n" + "    datanascita DATE NOT NULL,\n"
					+ "    regioneresidenza TEXT NOT NULL\n" + ");";
			stmt.execute(sql);

			sql = "CREATE TABLE fatture (\n" + "    numerofattura INTEGER PRIMARY KEY,\n"
					+ "    tipologia TEXT NOT NULL,\n" + "    importo REAL NOT NULL,\n" + "    iva REAL NOT NULL,\n"
					+ "    idcliente INTEGER NOT NULL,\n" + "    datafattura DATE NOT NULL,\n"
					+ "    numerofornitore INTEGER NOT NULL,\n"
					+ "    FOREIGN KEY (idcliente) REFERENCES clienti(numerocliente),\n"
					+ "    FOREIGN KEY (numerofornitore) REFERENCES fornitori(numerofornitore)\n" + ");";
			stmt.execute(sql);

			sql = "CREATE TABLE prodotti (\n" + "    idprodotto INTEGER PRIMARY KEY,\n"
					+ "    descrizione TEXT NOT NULL,\n" + "    inproduzione BOOLEAN NOT NULL,\n"
					+ "    incommercio BOOLEAN NOT NULL,\n" + "    dataattivazione DATE NOT NULL,\n"
					+ "    datadisattivazione DATE\n" + ");";
			stmt.execute(sql);

			sql = "CREATE TABLE fornitori (\n" + "    numerofornitore INTEGER PRIMARY KEY,\n"
					+ "    denominazione TEXT NOT NULL,\n" + "    regioneresidenza TEXT NOT NULL\n" + ");";
			stmt.execute(sql);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
