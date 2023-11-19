/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.BDD;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.utils.list.ListUtils;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.utils.exceptions.ExceptionsUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author laelt
 */
public class GestionBDD {
    
    private Connection conn;
    
    public GestionBDD(Connection conn) {
        this.conn = conn;
    }
    public static Connection connectGeneralMySQL(String host,int port, String database, String user, String pass) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }
    
    public static Connection connectSurServeurM3() throws SQLException {
        return connectGeneralMySQL("92.222.25.165", 3306,
                "m3_ltroullier01", "m3_ltroullier01",
                "cd8947dc");
    }
    
    public void creeTableMachine() throws SQLException {
        this.conn.setAutoCommit(false);
        System.out.println("Nom de la table : ");
        String nom = Lire.S();
        try (Statement st = this.conn.createStatement()) {
            st.executeUpdate(
                    "create table "+nom+" (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    ref varchar(30) not null unique,\n"
                    + "    des varchar(30) not null,\n"
                    + "    puissance float not null\n"
                    + ")\n"
            );
            this.conn.commit();
            System.out.println("TABLE CREE");
        } catch (SQLException ex) {
            this.conn.rollback();
            throw ex;
        } finally {
            this.conn.setAutoCommit(true);
        }
    }
    
    
    public void deleteTable() throws SQLException {
        System.out.println("Nom de la table : ");
        String nom = Lire.S();
        try (Statement st = this.conn.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate("drop table "+nom);
                System.out.println("TABLE SUPPRIMEE");
            } catch (SQLException ex) {
                System.out.println("Cette table n'existe pas");
            }
        }
    }

    public void creeSchemaEntier() throws SQLException {
        this.conn.setAutoCommit(false);
        try (Statement st = this.conn.createStatement()) {
            st.executeUpdate(
                    "create table r_machine (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    ref varchar(30) not null unique,\n"
                    + "    des varchar(30) not null,\n"
                    + "    puissance float not null\n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table r_operations (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    idtype integer not null,\n"
                    +"     idproduit integer not null\n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table r_precede (\n"
                    + "    opavant integer not null,\n"
                    +"     opapres integer not null\n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table r_produit (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    ref varchar(30) not null unique,\n"
                    + "    des varchar(30) not null\n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table r_realise (\n"
                    + "    idmachine integer not null,\n"
                    + "    idtype integer not null,\n"
                    + "    duree integer not null\n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table typeoperation2 (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    +"     des varchar(30) not null\n"
                    + ")\n"
            );
            this.conn.commit();
            st.executeUpdate(
                    "alter table operations2 \n"
                    +"  add constraint fk_operation2_idproduit \n"
                    +"  foreign key (idproduit) references produit2(id) \n"
            );
            st.executeUpdate(
                    "alter table operations2 \n"
                    +"  add constraint fk_operation2_idtype \n"
                    +"  foreign key (idtype) references typeoperation2(id) \n"
            );
            st.executeUpdate(
                    "alter table precede2 \n"
                    +"  add constraint fk_precede2_opapres \n"
                    +"  foreign key (opapres) references operations2(id) \n"
            );
            st.executeUpdate(
                    "alter table precede2 \n"
                    +"  add constraint fk_precede2_opavant \n"
                    +"  foreign key (opavant) references operations2(id) \n"
            );
            st.executeUpdate(
                    "alter table realise2 \n"
                    +"  add constraint fk_realise2_idmachine \n"
                    +"  foreign key (idmachine) references machine2(id) \n"
            );
            st.executeUpdate(
                    "alter table realise2 \n"
                    +"  add constraint fk_realise2_idtype \n"
                    +"  foreign key (idtype) references typeoperation2(id) \n"
            );
        } catch (SQLException ex) {
            this.conn.rollback();
            System.out.println("AIE");
            throw ex;
        } finally {
            this.conn.setAutoCommit(true);
        }
    }

    public void deleteSchemaEntier() throws SQLException {
        try (Statement st = this.conn.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate("alter table operations2 drop constraint fk_operation2_idproduit");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}
            try {
                st.executeUpdate("alter table operations2 drop constraint fk_operation2_idtype");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}
            try {
                st.executeUpdate("alter table precede2 drop constraint fk_precede2_opapres");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}
            try {
                st.executeUpdate("alter table precede2 drop constraint fk_precede2_opavant");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}
            try {
                st.executeUpdate("alter table realise2 drop constraint fk_realise2_idmachine");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}
            try {
                st.executeUpdate("alter table realise2 drop constraint fk_realise2_idtype");
            } catch (SQLException ex) {System.out.println("Contrainte inexistante");}

            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate("drop table machine2");
                st.executeUpdate("drop table operations2");
                st.executeUpdate("drop table precede2");
                st.executeUpdate("drop table produit2");
                st.executeUpdate("drop table realise2");
                st.executeUpdate("drop table typeoperation2");
                System.out.println("SCHEMA SUPPRIMEE");
            } catch (SQLException ex) {
                System.out.println("Cette table n'existe pas");
            }
        }
    }
    
    public void menuMachine() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println(" ");
            System.out.println("Menu machine");
            System.out.println("================");
            System.out.println((i++) + ") lister les machines");
            System.out.println((i++) + ") ajouter une machine");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    List<Machine> machines = Machine.ToutesLesMachines(this.conn);
                    System.out.println("");
                    System.out.println("Liste des machines : ");
                    System.out.println(ListUtils.enumerateList(machines));
                } else if (rep == j++) {
                    Machine nouveau = Machine.nouvelleMachine();
                    nouveau.saveInDBV1(this.conn);
                    System.out.println("MACHINE AJOUTEE");
                }
            } catch (SQLException ex) {
                System.out.println("ERREUR DANS LA CREATION DE LA MACHINE");
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.truollier", 5));
            }
        }
    }
    
    public void menuTypeoperation() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println(" ");
            System.out.println("Menu Type Operation");
            System.out.println("================");
            System.out.println((i++) + ") Ajouter un type d'operation");
            System.out.println((i++) + ") Lister les type d'operation");
            System.out.println((i++) + ") Associer un type d'ope a une machine");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    Typeoperation ope = Typeoperation.nouveautype();
                    ope.saveInDBV1(conn);
                    System.out.println("Type d'ope AJOUTE");
                }else if (rep == j++){
                    System.out.println(" ");
                    System.out.println("Liste des type d'operation :");
                    System.out.println(ListUtils.enumerateList(Typeoperation.ToutesLestypesOperations(this.conn)));
                }else if (rep == j++){
                    int idMachine = Machine.choixmachine(this.conn);
                    int idTypeoperation = Typeoperation.choixtypeoperation(this.conn);
                    Realise NvRealise = Realise.NouveauRealise(idMachine, idTypeoperation);
                    NvRealise.saveInDBV1(this.conn);
                }
            }catch (SQLException ex) {
                System.out.println("ERREUR DANS MENU TYPE OPERATION");
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.truollier", 5));
            }
        }
     }
    
    public void menuProduit(){
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println(" ");
            System.out.println("Menu Produit");
            System.out.println("================");
            System.out.println((i++) + ") Ajouter un produit");
            System.out.println((i++) + ") Lister les produits");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    Produit prod = Produit.NouveauProduit();
                    prod.saveInDBV1(conn);
                    System.out.println("Produit AJOUTE");
                }else if (rep == j++){
                    System.out.println(" ");
                    System.out.println("Liste des produits :");
                    System.out.println(ListUtils.enumerateList(Produit.ToutLesProduits(this.conn)));
                }
            }catch (SQLException ex) {
                System.out.println("ERREUR DANS MENU PRODUIT");
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.truollier", 5));
            }
        }
    }
    
    public void menuOperation (){
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println(" ");
            System.out.println("Menu Operation");
            System.out.println("================");
            System.out.println((i++) + ") Créer une opérations");
            System.out.println((i++) + ") Lister les opérations");
            System.out.println((i++) + ") Creer une succesion de deux opérations");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    System.out.println(" ");
                    int idProduit = Produit.ChoixProduit(this.conn);
                    int idTypeoperation = Typeoperation.choixtypeoperation(this.conn);
                    Operation NvOperation = Operation.NouvelleOperation(idTypeoperation, idProduit);
                    NvOperation.saveInDBV1(this.conn);
                }else if (rep == j++){
                    System.out.println(" ");
                    System.out.println("Liste des Operations :");
                    System.out.println(ListUtils.enumerateList(Operation.ToutesLesOPerations(this.conn)));
                }else if (rep == j++){
                    System.out.println(ListUtils.enumerateList(Operation.ToutesLesOPerations(this.conn)));                    
                    System.out.println("Chosir la 1ere opération :");
                    int opavant = Lire.i();
                    System.out.println(ListUtils.enumerateList(Operation.ToutesLesOPerations(this.conn)));                    
                    System.out.println("Chosir la 2eme opération :");
                    int opapres = Lire.i();
                    Precede pre = new Precede(opavant, opapres);
                    pre.saveInDBV1(this.conn);
                }
            }catch (SQLException ex) {
                System.out.println("ERREUR DANS MENU OPERATION");
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.truollier", 5));
            }
        }
    }
    
    public void menuPrincipal() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println(" ");
            System.out.println("Menu principal");
            System.out.println("==============");
            System.out.println((i++) + ") Supprimer schéma entier");
            System.out.println((i++) + ") Supprimer une table sans contrainte");
            System.out.println((i++) + ") Creer schéma en entier");
            System.out.println((i++) + ") Créer table de type machine");
            System.out.println((i++) + ") Gestion des machines");
            System.out.println((i++) + ") Gestion Type operation");
            System.out.println((i++) + ") Gestion des produits");
            System.out.println((i++) + ") Gestion des operations");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    this.deleteSchemaEntier();
                } else if (rep == j++) {
                    this.deleteTable();
                } else if (rep == j++) {
                    this.creeSchemaEntier();
                } else if (rep == j++) {
                    this.creeTableMachine();
                }else if (rep == j++) {
                    this.menuMachine();
                }
                else if (rep == j++) {
                    this.menuTypeoperation();
                }
                else if (rep == j++){
                    this.menuProduit();
                }
                else if (rep == j++){
                    this.menuOperation();
                }
            } catch (SQLException ex) {
                System.out.println("erreur menu");
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.troullier", 5));
            }
        }
    }
    
    public static void debut() {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("connecté");
            GestionBDD gestionnaire = new GestionBDD(con);
            gestionnaire.menuPrincipal();
        } catch (SQLException ex) {
            throw new Error("Connection impossible", ex);
        }
    }
}
