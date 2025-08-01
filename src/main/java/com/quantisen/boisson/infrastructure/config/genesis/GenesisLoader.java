package com.quantisen.boisson.infrastructure.config.genesis;

import com.quantisen.boisson.application.boisson.services.BoissonService;
import com.quantisen.boisson.application.fournisseur.services.FournisseurService;
import com.quantisen.boisson.application.identite.services.IdentiteService;
import com.quantisen.boisson.application.stockage.services.StockageService;
import com.quantisen.boisson.domaine.identite.domainModel.Role;
import com.quantisen.boisson.web.boisson.dtos.BoissonDto;
import com.quantisen.boisson.web.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.web.identite.dtos.IdentiteDto;
import com.quantisen.boisson.web.stockage.dtos.LotDto;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class GenesisLoader implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(GenesisLoader.class.getName());

    @Inject
    private IdentiteService identiteService;
    @Inject
    private BoissonService boissonService;
    @Inject
    private StockageService stockageService;
    @Inject
    private FournisseurService fournisseurService;

    @PostConstruct
    public void init() {
        try {
            log.info("QuantiSen application started");
            createUtilisateurs();
            createBoissons();
            createFournisseurs();
            createInventaire();
        } catch (Exception e) {
            log.error("Error during data seeding: " + e.getMessage(), e);
        }
    }


    private void createInventaire() {
        if (!stockageService.getAllLots().isEmpty()) {
            log.info("Inventaire already exists, skipping creation.");
            return;
        }
        LotDto lot1 = new LotDto();
        lot1.setNumeroLot("L001");
        lot1.setQuantiteInitiale(100);
        lot1.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(6)));
        lot1.setBoisson(boissonService.getBoissonByNom("Coca-Cola"));
        lot1.setVendable(true);
        lot1.setFournisseur(fournisseurService.rechercherFournisseurParNom("Le Marché"));

        LotDto lot2 = new LotDto();
        lot2.setNumeroLot("L002");
        lot2.setQuantiteInitiale(150);
        lot2.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(8)));
        lot2.setBoisson(boissonService.getBoissonByNom("Pepsi"));
        lot2.setVendable(true);
        lot2.setFournisseur(fournisseurService.rechercherFournisseurParNom("Le Marché"));

        List<LotDto> lots = Arrays.asList(lot1, lot2);
        stockageService.entreeBatch(lots, identiteService.getAll().get(0));

        LotDto lot3 = new LotDto();
        lot3.setNumeroLot("L003");
        lot3.setQuantiteInitiale(200);
        lot3.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(12)));
        lot3.setBoisson(boissonService.getBoissonByNom("Fanta"));
        lot3.setVendable(true);
        lot3.setFournisseur(fournisseurService.rechercherFournisseurParNom("Super Marché"));

        LotDto lot4 = new LotDto();
        lot4.setNumeroLot("L004");
        lot4.setQuantiteInitiale(120);
        lot4.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(10)));
        lot4.setBoisson(boissonService.getBoissonByNom("Sprite"));
        lot4.setVendable(true);
        lot4.setFournisseur(fournisseurService.rechercherFournisseurParNom("Super Marché"));

        LotDto lot5 = new LotDto();
        lot5.setNumeroLot("L005");
        lot5.setQuantiteInitiale(80);
        lot5.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(9)));
        lot5.setBoisson(boissonService.getBoissonByNom("Orangina"));
        lot5.setVendable(true);
        lot5.setFournisseur(fournisseurService.rechercherFournisseurParNom("Super Marché"));

        LotDto lot6 = new LotDto();
        lot6.setNumeroLot("L006");
        lot6.setQuantiteInitiale(90);
        lot6.setDatePeremption(String.valueOf(LocalDate.now().plusMonths(11)));
        lot6.setBoisson(boissonService.getBoissonByNom("Oasis"));
        lot6.setVendable(true);
        lot6.setFournisseur(fournisseurService.rechercherFournisseurParNom("Super Marché"));

        List<LotDto> lots2 = Arrays.asList(lot3, lot4, lot5, lot6);
        stockageService.entreeBatch(lots2, identiteService.getAll().get(1));
    }

    private void createBoissons() {
        if (!boissonService.getAllBoisson().isEmpty()) {
            log.info("Boissons already exist, skipping creation.");
            return;
        }
        BoissonDto coca = new BoissonDto();
        coca.setNom("Coca-Cola");
        coca.setPrix(250);
        coca.setDescription("Boisson gazeuse rafraîchissante");
        coca.setActive(true);
        coca.setUnite("ml");
        coca.setVolume(330);
        coca.setSeuil(15);
        boissonService.ajouterBoisson(coca);

        BoissonDto pepsi = new BoissonDto();
        pepsi.setNom("Pepsi");
        pepsi.setPrix(340);
        pepsi.setActive(true);
        pepsi.setUnite("ml");
        pepsi.setVolume(330);
        pepsi.setSeuil(10);

        boissonService.ajouterBoisson(pepsi);

        BoissonDto fanta = new BoissonDto();
        fanta.setNom("Fanta");
        fanta.setDescription("Boisson gazeuse à l'orange");
        fanta.setPrix(200);
        fanta.setActive(true);
        fanta.setUnite("ml");
        fanta.setVolume(330);
        fanta.setSeuil(10);

        boissonService.ajouterBoisson(fanta);

        BoissonDto sprite = new BoissonDto();
        sprite.setNom("Sprite");
        sprite.setDescription("Boisson gazeuse citron-lime");
        sprite.setPrix(220);
        sprite.setActive(true);
        sprite.setUnite("ml");
        sprite.setVolume(330);
        sprite.setSeuil(10);

        boissonService.ajouterBoisson(sprite);

        BoissonDto Orangina = new BoissonDto();
        Orangina.setNom("Orangina");
        Orangina.setDescription("Boisson gazeuse à l'orange avec pulpe");
        Orangina.setPrix(300);
        Orangina.setActive(true);
        Orangina.setUnite("ml");
        Orangina.setVolume(330);
        Orangina.setSeuil(20);

        boissonService.ajouterBoisson(Orangina);

        BoissonDto oasis = new BoissonDto();
        oasis.setNom("Oasis");
        oasis.setDescription("Boisson gazeuse fruitée");
        oasis.setPrix(280);
        oasis.setActive(true);
        oasis.setUnite("ml");
        oasis.setVolume(330);
        oasis.setSeuil(15);

        boissonService.ajouterBoisson(oasis);
    }

    private void createUtilisateurs() {
        if (identiteService.getAll().size() > 1) {
            log.info("Users already exist, skipping creation.");
            return;
        }
        String email = "houleymatou@boisson.com";
        String email2 = "atidiane741@boisson.com";

        IdentiteDto admin = new IdentiteDto();
        admin.setEmail(email);
        admin.setMotDePasse("Passer123");
        admin.setRole(Role.GERANT);
        identiteService.register(admin);
        log.info("Admin user 1 created.");
        IdentiteDto admin1 = new IdentiteDto();
        admin1.setEmail(email2);
        admin1.setMotDePasse("Passer123");
        admin1.setRole(Role.GERANT);
        identiteService.register(admin1);
        log.info("Admin user 2 created.");
    }

    private void createFournisseurs() {
        if (!fournisseurService.recupererTousLesFournisseurs().isEmpty()) {
            log.info("Fournisseurs already exist, skipping creation.");
            return;
        }

        FournisseurDto f1 = new FournisseurDto();
        f1.setNom("Le Marché");
        f1.setAdresse("Dakar, Senegal");
        f1.setContact("771234567");
        fournisseurService.enregistrerNouveauFournisseur(f1);

        FournisseurDto f2 = new FournisseurDto();
        f2.setNom("Super Marché");
        f2.setAdresse("Dakar, Senegal");
        f2.setContact("771234568");
        fournisseurService.enregistrerNouveauFournisseur(f2);
    }
}