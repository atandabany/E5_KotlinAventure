package com.example.springaventure.controller.admin

import com.example.springaventure.model.dao.PotionDao
import com.example.springaventure.model.entity.Potion
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Contrôleur responsable de la gestion des potions dans la partie administrative de l'application.
 */
@Controller
class PotionControleur(
    /** DAO pour l'accès aux données des potions. */
    val potionDao: PotionDao
) {

    /**
     * Affiche la liste des potions dans la page d'index.
     *
     * @param model Modèle utilisé pour transmettre des données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/admin/potion")
    fun index(model: Model): String {
        // Récupère toutes les potions depuis la base de données
        val potions = this.potionDao.findAll()
        // Ajoute la liste des potions au modèle pour transmission à la vue
        model.addAttribute("potions", potions)
        // Retourne le nom de la vue à afficher
        return "admin/potion/index"
    }

    /**
     * Affiche les détails d'une potion en particulier.
     *
     * @param id L'identifiant unique de la potion à afficher.
     * @param model Le modèle utilisé pour transmettre les données à la vue.
     * @return Le nom de la vue à afficher.
     * @throws NoSuchElementException si la potion avec l'ID spécifié n'est pas trouvée.
     */
    @GetMapping("/admin/potion/{id}")
    fun show(@PathVariable id: Long, model: Model): String {
        // Récupère la potion avec l'ID spécifié depuis la base de données
        val unePotion = this.potionDao.findById(id).orElseThrow()

        // Ajoute la potion au modèle pour affichage dans la vue
        model.addAttribute("potion", unePotion)

        // Retourne le nom de la vue à afficher
        return "admin/potion/show"
    }

    /**
     * Affiche le formulaire de création d'une nouvelle potion.
     *
     * @param model Le modèle utilisé pour transmettre les données à la vue.
     * @return Le nom de la vue à afficher (le formulaire de création).
     */
    @GetMapping("/admin/potion/create")
    fun create(model: Model): String {
        // Crée une nouvelle instance de Potion avec des valeurs par défaut
        val nouvellePotion = Potion(null, "", "", "", 0)

        // Ajoute la nouvelle potion au modèle pour affichage dans le formulaire de création
        model.addAttribute("nouvellePotion", nouvellePotion)

        // Retourne le nom de la vue à afficher (le formulaire de création)
        return "admin/potion/create"
    }

    /**
     * Gère la soumission du formulaire d'ajout de potion.
     *
     * @param nouvellePotion L'objet Potion créé à partir des données du formulaire.
     * @param redirectAttributes Les attributs de redirection pour transmettre des messages à la vue suivante.
     * @return La redirection vers la page d'administration des potions après l'ajout réussi.
     */
    @PostMapping("/admin/potion")
    fun store(@ModelAttribute nouvellePotion: Potion, redirectAttributes: RedirectAttributes): String {
        // Sauvegarde la nouvelle potion dans la base de données
        val savedPotion = this.potionDao.save(nouvellePotion)
        // Ajoute un message de succès pour être affiché à la vue suivante
        redirectAttributes.addFlashAttribute("msgSuccess", "Enregistrement de ${savedPotion.nom} réussi")
        // Redirige vers la page d'administration des potions
        return "redirect:/admin/potion"
    }

    @GetMapping("/admin/potion/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model): String {
        // Récupère la potion avec l'ID spécifié depuis la base de données
        val unePotion = this.potionDao.findById(id).orElseThrow()

        // Ajoute la potion au modèle pour affichage dans la vue
        model.addAttribute("potion", unePotion)

        // Retourne le nom de la vue à afficher
        return "admin/potion/edit"
    }

    /**
     * Gère la soumission du formulaire de mise à jour de potion.
     *
     * @param potion L'objet Potion mis à jour à partir des données du formulaire.
     * @param redirectAttributes Les attributs de redirection pour transmettre des messages à la vue suivante.
     * @return La redirection vers la page d'administration des potions après la mise à jour réussie.
     * @throws NoSuchElementException si la potion avec l'ID spécifié n'est pas trouvée dans la base de données.
     */
    @PostMapping("/admin/potion/update")
    fun update(@ModelAttribute potion: Potion, redirectAttributes: RedirectAttributes): String {
        // Recherche de la potion existante dans la base de données
        val potionModifier = this.potionDao.findById(potion.id ?: 0).orElseThrow()

        // Mise à jour des propriétés de la potion avec les nouvelles valeurs du formulaire
        potionModifier.nom = potion.nom
        potionModifier.description = potion.description
        potionModifier.soin = potion.soin

        // Sauvegarde la potion modifiée dans la base de données
        val savedPotion = this.potionDao.save(potionModifier)

        // Ajoute un message de succès pour être affiché à la vue suivante
        redirectAttributes.addFlashAttribute("msgSuccess", "Modification de ${savedPotion.nom} réussie")

        // Redirige vers la page d'administration des potions
        return "redirect:/admin/potion"
    }

    /**
     * Gère la suppression d'une potion par son identifiant.
     *
     * @param id L'identifiant de la potion à supprimer.
     * @param redirectAttributes Les attributs de redirection pour transmettre des messages à la vue suivante.
     * @return La redirection vers la page d'administration des potions après la suppression réussie.
     * @throws NoSuchElementException si la potion avec l'ID spécifié n'est pas trouvée dans la base de données.
     */
    @PostMapping("/admin/potion/delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        // Recherche de la potion à supprimer dans la base de données
        val potion: Potion = this.potionDao.findById(id).orElseThrow()

        // Suppression de la potion de la base de données
        this.potionDao.delete(potion)

        // Ajoute un message de succès pour être affiché à la vue suivante
        redirectAttributes.addFlashAttribute("msgSuccess", "Suppression de ${potion.nom} réussie")

        // Redirige vers la page d'administration des potions
        return "redirect:/admin/potion"
    }
}
