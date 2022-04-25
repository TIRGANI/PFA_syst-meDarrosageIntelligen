import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Historique = () => import('@/entities/historique/historique.vue');
// prettier-ignore
const HistoriqueUpdate = () => import('@/entities/historique/historique-update.vue');
// prettier-ignore
const HistoriqueDetails = () => import('@/entities/historique/historique-details.vue');
// prettier-ignore
const TypeSol = () => import('@/entities/type-sol/type-sol.vue');
// prettier-ignore
const TypeSolUpdate = () => import('@/entities/type-sol/type-sol-update.vue');
// prettier-ignore
const TypeSolDetails = () => import('@/entities/type-sol/type-sol-details.vue');
// prettier-ignore
const Parcelle = () => import('@/entities/parcelle/parcelle.vue');
// prettier-ignore
const ParcelleUpdate = () => import('@/entities/parcelle/parcelle-update.vue');
// prettier-ignore
const ParcelleDetails = () => import('@/entities/parcelle/parcelle-details.vue');
// prettier-ignore
const Ferm = () => import('@/entities/ferm/ferm.vue');
// prettier-ignore
const FermUpdate = () => import('@/entities/ferm/ferm-update.vue');
// prettier-ignore
const FermDetails = () => import('@/entities/ferm/ferm-details.vue');
// prettier-ignore
const Alerte = () => import('@/entities/alerte/alerte.vue');
// prettier-ignore
const AlerteUpdate = () => import('@/entities/alerte/alerte-update.vue');
// prettier-ignore
const AlerteDetails = () => import('@/entities/alerte/alerte-details.vue');
// prettier-ignore
const Affectation = () => import('@/entities/affectation/affectation.vue');
// prettier-ignore
const AffectationUpdate = () => import('@/entities/affectation/affectation-update.vue');
// prettier-ignore
const AffectationDetails = () => import('@/entities/affectation/affectation-details.vue');
// prettier-ignore
const Grandeur = () => import('@/entities/grandeur/grandeur.vue');
// prettier-ignore
const GrandeurUpdate = () => import('@/entities/grandeur/grandeur-update.vue');
// prettier-ignore
const GrandeurDetails = () => import('@/entities/grandeur/grandeur-details.vue');
// prettier-ignore
const Plantage = () => import('@/entities/plantage/plantage.vue');
// prettier-ignore
const PlantageUpdate = () => import('@/entities/plantage/plantage-update.vue');
// prettier-ignore
const PlantageDetails = () => import('@/entities/plantage/plantage-details.vue');
// prettier-ignore
const Plante = () => import('@/entities/plante/plante.vue');
// prettier-ignore
const PlanteUpdate = () => import('@/entities/plante/plante-update.vue');
// prettier-ignore
const PlanteDetails = () => import('@/entities/plante/plante-details.vue');
// prettier-ignore
const TypePlant = () => import('@/entities/type-plant/type-plant.vue');
// prettier-ignore
const TypePlantUpdate = () => import('@/entities/type-plant/type-plant-update.vue');
// prettier-ignore
const TypePlantDetails = () => import('@/entities/type-plant/type-plant-details.vue');
// prettier-ignore
const Boitier = () => import('@/entities/boitier/boitier.vue');
// prettier-ignore
const BoitierUpdate = () => import('@/entities/boitier/boitier-update.vue');
// prettier-ignore
const BoitierDetails = () => import('@/entities/boitier/boitier-details.vue');
// prettier-ignore
const Brache = () => import('@/entities/brache/brache.vue');
// prettier-ignore
const BracheUpdate = () => import('@/entities/brache/brache-update.vue');
// prettier-ignore
const BracheDetails = () => import('@/entities/brache/brache-details.vue');
// prettier-ignore
const Capteur = () => import('@/entities/capteur/capteur.vue');
// prettier-ignore
const CapteurUpdate = () => import('@/entities/capteur/capteur-update.vue');
// prettier-ignore
const CapteurDetails = () => import('@/entities/capteur/capteur-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'historique',
      name: 'Historique',
      component: Historique,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historique/new',
      name: 'HistoriqueCreate',
      component: HistoriqueUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historique/:historiqueId/edit',
      name: 'HistoriqueEdit',
      component: HistoriqueUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historique/:historiqueId/view',
      name: 'HistoriqueView',
      component: HistoriqueDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-sol',
      name: 'TypeSol',
      component: TypeSol,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-sol/new',
      name: 'TypeSolCreate',
      component: TypeSolUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-sol/:typeSolId/edit',
      name: 'TypeSolEdit',
      component: TypeSolUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-sol/:typeSolId/view',
      name: 'TypeSolView',
      component: TypeSolDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'parcelle',
      name: 'Parcelle',
      component: Parcelle,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'parcelle/new',
      name: 'ParcelleCreate',
      component: ParcelleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'parcelle/:parcelleId/edit',
      name: 'ParcelleEdit',
      component: ParcelleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'parcelle/:parcelleId/view',
      name: 'ParcelleView',
      component: ParcelleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ferm',
      name: 'Ferm',
      component: Ferm,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ferm/new',
      name: 'FermCreate',
      component: FermUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ferm/:fermId/edit',
      name: 'FermEdit',
      component: FermUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ferm/:fermId/view',
      name: 'FermView',
      component: FermDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte',
      name: 'Alerte',
      component: Alerte,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/new',
      name: 'AlerteCreate',
      component: AlerteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/:alerteId/edit',
      name: 'AlerteEdit',
      component: AlerteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/:alerteId/view',
      name: 'AlerteView',
      component: AlerteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'affectation',
      name: 'Affectation',
      component: Affectation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'affectation/new',
      name: 'AffectationCreate',
      component: AffectationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'affectation/:affectationId/edit',
      name: 'AffectationEdit',
      component: AffectationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'affectation/:affectationId/view',
      name: 'AffectationView',
      component: AffectationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grandeur',
      name: 'Grandeur',
      component: Grandeur,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grandeur/new',
      name: 'GrandeurCreate',
      component: GrandeurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grandeur/:grandeurId/edit',
      name: 'GrandeurEdit',
      component: GrandeurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grandeur/:grandeurId/view',
      name: 'GrandeurView',
      component: GrandeurDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plantage',
      name: 'Plantage',
      component: Plantage,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plantage/new',
      name: 'PlantageCreate',
      component: PlantageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plantage/:plantageId/edit',
      name: 'PlantageEdit',
      component: PlantageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plantage/:plantageId/view',
      name: 'PlantageView',
      component: PlantageDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plante',
      name: 'Plante',
      component: Plante,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plante/new',
      name: 'PlanteCreate',
      component: PlanteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plante/:planteId/edit',
      name: 'PlanteEdit',
      component: PlanteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plante/:planteId/view',
      name: 'PlanteView',
      component: PlanteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-plant',
      name: 'TypePlant',
      component: TypePlant,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-plant/new',
      name: 'TypePlantCreate',
      component: TypePlantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-plant/:typePlantId/edit',
      name: 'TypePlantEdit',
      component: TypePlantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'type-plant/:typePlantId/view',
      name: 'TypePlantView',
      component: TypePlantDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'boitier',
      name: 'Boitier',
      component: Boitier,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'boitier/new',
      name: 'BoitierCreate',
      component: BoitierUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'boitier/:boitierId/edit',
      name: 'BoitierEdit',
      component: BoitierUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'boitier/:boitierId/view',
      name: 'BoitierView',
      component: BoitierDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brache',
      name: 'Brache',
      component: Brache,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brache/new',
      name: 'BracheCreate',
      component: BracheUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brache/:bracheId/edit',
      name: 'BracheEdit',
      component: BracheUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brache/:bracheId/view',
      name: 'BracheView',
      component: BracheDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capteur',
      name: 'Capteur',
      component: Capteur,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capteur/new',
      name: 'CapteurCreate',
      component: CapteurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capteur/:capteurId/edit',
      name: 'CapteurEdit',
      component: CapteurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capteur/:capteurId/view',
      name: 'CapteurView',
      component: CapteurDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
