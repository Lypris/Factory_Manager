package fr.insa.trenchant_troullier_virquin.applicationwebm3.BDD;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.Lire;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.FakeDataGenerator;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.Status_Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.utils.list.ListUtils;
import org.springframework.data.repository.query.Param;

public class Gestion {
    private Connection conn;

    public Gestion(Connection conn) {
        this.conn = conn;
    }

    //method to connect to the database connectGeneralMySQL with host, port and pass
    public static Connection connectGeneralMySQL(String host, int port, String database, String user, String pass) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port
                        + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection connectSurServeur() throws SQLException {
        return connectGeneralMySQL("92.222.25.165", 3306,
                "m3_rvirquin01", "m3_rvirquin01",
                "a89a0015");
    }








    //method to create the schema of the database explicitely
    public void createSchema() throws SQLException {
        this.conn.setAutoCommit(false);
        try (Statement stmt = this.conn.createStatement()) {
            stmt.executeUpdate(
                    """
                                            create table r_machine (
                                                id integer not null primary key AUTO_INCREMENT,
                                                ref varchar(30) not null unique,
                                                des varchar(150) not null,
                                                puissance float not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_type_operation (
                                                id integer not null primary key AUTO_INCREMENT,
                                                des varchar(150) not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_realise (
                                                idmachine integer not null,
                                                idtype integer not null,
                                                duree float not null
                                                
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_operations (
                                                id integer not null primary key AUTO_INCREMENT,
                                                ref varchar(30) not null unique,
                                                idtype integer not null,
                                                idproduit integer not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_produit (
                                                id integer not null primary key AUTO_INCREMENT,
                                                ref varchar(30) not null unique,
                                                des varchar(150) not null,
                                                prix float not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_etat_possible_machine (
                                                id integer not null primary key AUTO_INCREMENT,
                                                des varchar(150) not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_etatmachine (
                                                id integer not null primary key AUTO_INCREMENT,
                                                debut timestamp not null,
                                                fin timestamp not null,
                                                idmachine integer not null,
                                                idetatpossible integer not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_operateur (
                                                id integer not null primary key AUTO_INCREMENT,
                                                nom varchar(30) not null,
                                                prenom varchar(30) not null,
                                                mail varchar(60) not null,
                                                tel varchar(30) not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_etat_possible_operateur (
                                                id integer not null primary key AUTO_INCREMENT,
                                                des varchar(150) not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_etat_operateur (
                                                id integer not null primary key AUTO_INCREMENT,
                                                debut timestamp not null,
                                                fin timestamp not null,
                                                idoperateur integer not null,
                                                idetatpossible integer not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_precedence_operation (
                                                idoperation_avant integer not null,
                                                idoperation_apres integer not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_poste_travail (
                                                id integer not null primary key AUTO_INCREMENT,
                                                ref varchar(30) not null unique,
                                                des varchar(150) not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_exemplaire (
                                                id integer not null primary key AUTO_INCREMENT,
                                                num_serie varchar(30) not null unique,
                                                idproduit integer not null
                                            )
                            """);
            stmt.executeUpdate(
                    """
                                            create table r_operation_effectuee (
                                                id integer not null primary key AUTO_INCREMENT,
                                                debut timestamp not null,
                                                fin timestamp not null,
                                                idoperation integer not null,
                                                idexemplaire integer not null,
                                                idmachine integer not null
                                            )
                            """);
            //si j'arrive ici, c'est que tout s'est bien passé donc je commit
            this.conn.commit();
            // on gère les clés étrangères
            stmt.executeUpdate(
                    """
                ALTER TABLE r_realise add constraint fk_r_realise_idmachine foreign key (idmachine) references r_machine(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_realise add constraint fk_r_realise_idtype foreign key (idtype) references r_type_operation(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_operations add constraint fk_r_operations_idtype foreign key (idtype) references r_type_operation(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_operations add constraint fk_r_operations_idproduit foreign key (idproduit) references r_produit(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_etatmachine add constraint fk_r_etatmachine_idmachine foreign key (idmachine) references r_machine(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_etatmachine add constraint fk_r_etatmachine_idetatpossible foreign key (idetatpossible) references r_etat_possible_machine(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_etat_operateur add constraint fk_r_etat_operateur_idoperateur foreign key (idoperateur) references r_operateur(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_etat_operateur add constraint fk_r_etat_operateur_idetatpossible foreign key (idetatpossible) references r_etat_possible_operateur(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_precedence_operation add constraint fk_r_precedence_operation_idoperation_avant foreign key (idoperation_avant) references r_operations(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_precedence_operation add constraint fk_r_precedence_operation_idoperation_apres foreign key (idoperation_apres) references r_operations(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_exemplaire add constraint fk_r_exemplaire_idproduit foreign key (idproduit) references r_produit(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee add constraint fk_r_operation_effectuee_idoperation foreign key (idoperation) references r_operations(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee add constraint r_operation_effectuee_idexemplaire foreign key (idexemplaire) references r_exemplaire(id)
                            """ );
            stmt.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee add constraint r_operation_effectuee_idmachine foreign key (idmachine) references r_machine(id)
                            """ );
            System.out.println("Schéma crée");
        } catch (SQLException ex) {
            this.conn.rollback();
            System.out.println("AIE");
            throw ex;
        } finally {
            this.conn.setAutoCommit(true);
        }
    }

    public void deleteSchema() throws SQLException {
        try (Statement st = this.conn.createStatement()) {
            //on commence par supprimer les clés étrangères
           st.executeUpdate(
                    """
                ALTER TABLE r_realise DROP FOREIGN KEY fk_r_realise_idmachine
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_realise DROP FOREIGN KEY fk_r_realise_idtype
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_operations DROP FOREIGN KEY fk_r_operations_idtype
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_operations DROP FOREIGN KEY fk_r_operations_idproduit
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_etatmachine DROP FOREIGN KEY fk_r_etatmachine_idmachine
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_etatmachine DROP FOREIGN KEY fk_r_etatmachine_idetatpossible
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_etat_operateur DROP FOREIGN KEY fk_r_etat_operateur_idoperateur
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_etat_operateur DROP FOREIGN KEY fk_r_etat_operateur_idetatpossible
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_precedence_operation DROP FOREIGN KEY fk_r_precedence_operation_idoperation_avant
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_precedence_operation DROP FOREIGN KEY fk_r_precedence_operation_idoperation_apres
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_exemplaire DROP FOREIGN KEY fk_r_exemplaire_idproduit
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee DROP FOREIGN KEY fk_r_operation_effectuee_idoperation
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee DROP FOREIGN KEY r_operation_effectuee_idexemplaire
                            """ );
            st.executeUpdate(
                    """
                ALTER TABLE r_operation_effectuee DROP FOREIGN KEY r_operation_effectuee_idmachine
                            """ );

            //puis on supprime les tables
            st.executeUpdate("DROP TABLE r_machine");
            st.executeUpdate("DROP TABLE r_type_operation");
            st.executeUpdate("DROP TABLE r_realise");
            st.executeUpdate("DROP TABLE r_operations");
            st.executeUpdate("DROP TABLE r_produit");
            st.executeUpdate("DROP TABLE r_etat_possible_machine");
            st.executeUpdate("DROP TABLE r_etatmachine");
            st.executeUpdate("DROP TABLE r_operateur");
            st.executeUpdate("DROP TABLE r_etat_possible_operateur");
            st.executeUpdate("DROP TABLE r_etat_operateur");
            st.executeUpdate("DROP TABLE r_precedence_operation");
            st.executeUpdate("DROP TABLE r_poste_travail");
            st.executeUpdate("DROP TABLE r_exemplaire");
            st.executeUpdate("DROP TABLE r_operation_effectuee");

            System.out.println("Schema deleted");
        }
    }

    public void menuPrincipal() throws SQLException {
        while(true){
            System.out.println("Veuillez choisir une option :");
            System.out.println("1. Créer le schéma de la base de données");
            System.out.println("2. Supprimer le schéma de la base de données");
            System.out.println("3. Accéder au menu machine");
            System.out.println("4. Accéder au menu de type d'opération");
            System.out.println("5. Accéder au menu d'opérateur");
            System.out.println("6. Quitter");
            int choix = Lire.i();
            //on traite le choix de l'utilisateur en fonction de chaque cas
            if(choix ==1){
                this.createSchema();
            }
            else if (choix==2){
                this.deleteSchema();
            }
            else if(choix==3){
                //this.menuMachine();
            }
            else if(choix==4){
               // this.menuTypeOperation();
            }
            else if(choix==5){
                this.menuOperateur();
            }
            else if(choix==6){
                System.out.println("Au revoir !");
                System.exit(0);
            }
        }
    }

    private void menuOperateur() {
        //choix entre ajouter, supprimer, modifier ou afficher
        System.out.println("Veuillez choisir une option :");
        System.out.println("1. Ajouter un opérateur");
        System.out.println("2. Supprimer un opérateur");
        System.out.println("3. Modifier un opérateur");
        System.out.println("4. Afficher les opérateurs");
        System.out.println("5. Générer un certain nombre d'opérateurs");
        System.out.println("6. Retour au menu principal");
        int choix = Lire.i();
        //on traite le choix de l'utilisateur en fonction de chaque cas
        if(choix ==1){
            try {
                Operateur op = Operateur.createOperateur(this.conn);
                //Status_Operateur status = Status_Operateur.createStatusOperateur(this.conn);
                op.saveInDB(this.conn);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        else if (choix==2){
            Operateur.deleteOperateur(this.conn);
        }
        else if(choix==3){
            Operateur.modifyOperateur(this.conn);
        }
        else if(choix==4){
           try {
               List<Operateur> oprateurs = Operateur.allOperateurs(this.conn);
               System.out.println("Liste des opérateurs : ");
               System.out.println(ListUtils.enumerateList(oprateurs));
           }
           catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
        else if(choix==5){
            System.out.println("Combien d'opérateurs voulez-vous générer ?");
            int nb = Lire.i();
            List<Operateur> operators = FakeDataGenerator.generateOperators(nb);
            for(Operateur op : operators ){
                try {
                    op.saveInDBV2(this.conn);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            System.out.println("Opérateurs générés");
        }
        else if(choix==6){
            try {
                this.menuPrincipal();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*
    public void menuMachine() throws SQLException{
        System.out.println("Bienvenue dans le menu machine");
        System.out.println("1. Créer une machine");
        System.out.println("2. Supprimer une machine");
        System.out.println("3. Afficher les machines");
        System.out.println("4. Associer une machine à un type d'opération");
        System.out.println("5. Retour au menu principal");
        int choix = Lire.i();
        //on traite le choix de l'utilisateur en fonction de chaque cas
        if(choix ==1){
            Machine.ajouterMachine(this.conn);
        }
        else if (choix==2){
            Machine.supprimerMachine(this.conn);
        }
        else if(choix==3){
            this.afficherMachine();
        }
        else if(choix==4){
            Machine.associerMachineTypeOperation(this.conn);
        }
        else if(choix==5){
            this.menuPrincipal();
        }
    }
    public void menuTypeOperation() throws SQLException{
        System.out.println("Bienvenue dans le menu type d'opération");
        System.out.println("1. Créer un type d'opération");
        System.out.println("2. Supprimer un type d'opération");
        System.out.println("3. Afficher les types d'opération");
        System.out.println("4. Retour au menu principal");
        int choix = Lire.i();
        //on traite le choix de l'utilisateur en fonction de chaque cas
        if(choix ==1){
            TypeOperation.ajouterTypeOperation(this.conn);
        }
        else if (choix==2){
            TypeOperation.supprimerTypeOperation(this.conn);
        }
        else if(choix==3){
            this.afficherTypeOperation();
        }
        else if(choix==4){
            this.menuPrincipal();
        }
    }

    private void afficherTypeOperation() throws SQLException{
        try (Statement stmt = this.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from r_type_operation");
            while (rs.next()) {
                System.out.println("Type d'opération : " + rs.getInt("id") + " " +rs.getString("des"));
            }
        }
    }

    public void afficherMachine () throws SQLException {
        try (Statement stmt = this.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from r_machine");
            while (rs.next()) {
                System.out.println("Machine : " + rs.getInt("id") + " " +rs.getString("ref") + " " + rs.getString("des") + " " + rs.getFloat("puissance"));
            }
        }
        catch (SQLException ex) {
            System.out.println("Erreur lors de l'affichage des machines");
            throw ex;
        }


    }

*/

    public static void debut() {
        try (Connection con = connectSurServeur()) {
            System.out.println("connecté");
            Gestion gestionnaire = new Gestion(con);
            gestionnaire.menuPrincipal();
        } catch (SQLException ex) {
            throw new Error("Connection impossible", ex);
        }
    }

    public List<Operateur> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            try {
                return Operateur.allOperateurs(this.conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return Operateur.searchAllOperateurs(this.conn, stringFilter);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
