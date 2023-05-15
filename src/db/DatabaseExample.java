package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseExample {
	public static void main(String[] args) {
		// sostituisci con l'URL del tuo database
		String url = "jdbc:postgresql://127.0.0.1:5432/database.db";

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

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {

			// 1. Estrarre il nome e il cognome dei clienti nati nel 1982
			String sql = "SELECT nome, cognome\n" + "FROM clienti\n" + "WHERE strftime('%Y', datanascita) = '1982';";
			ResultSet rs = stmt.executeQuery(sql);

			System.out.println("Clienti nati nel 1982:");
			while (rs.next()) {
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				System.out.println(nome + " " + cognome);
			}

			// 2. Estrarre il numero delle fatture con IVA al 20%
			sql = "SELECT COUNT(*)\n" + "FROM fatture\n" + "WHERE iva = 20;";
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Numero di fatture con IVA al 20%: " + count);
			}

			// 3. Riportare il numero di fatture e la somma dei relativi importi divisi per
			// anno di fatturazione
			sql = "SELECT strftime('%Y', datafattura) AS Anno, COUNT(*) AS NumeroFatture, SUM(importo) AS SommaImporti\n"
					+ "FROM fatture\n" + "GROUP BY Anno;";
			rs = stmt.executeQuery(sql);

			System.out.println("Fatture per anno:");
			while (rs.next()) {
				String anno = rs.getString("Anno");
				int numeroFatture = rs.getInt("NumeroFatture");
				double sommaImporti = rs.getDouble("SommaImporti");
				System.out.println(anno + ": " + numeroFatture + " fatture, totale importi: " + sommaImporti);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}