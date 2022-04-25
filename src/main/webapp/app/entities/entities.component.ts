import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import HistoriqueService from './historique/historique.service';
import TypeSolService from './type-sol/type-sol.service';
import ParcelleService from './parcelle/parcelle.service';
import FermService from './ferm/ferm.service';
import AlerteService from './alerte/alerte.service';
import AffectationService from './affectation/affectation.service';
import GrandeurService from './grandeur/grandeur.service';
import PlantageService from './plantage/plantage.service';
import PlanteService from './plante/plante.service';
import TypePlantService from './type-plant/type-plant.service';
import BoitierService from './boitier/boitier.service';
import BracheService from './brache/brache.service';
import CapteurService from './capteur/capteur.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('historiqueService') private historiqueService = () => new HistoriqueService();
  @Provide('typeSolService') private typeSolService = () => new TypeSolService();
  @Provide('parcelleService') private parcelleService = () => new ParcelleService();
  @Provide('fermService') private fermService = () => new FermService();
  @Provide('alerteService') private alerteService = () => new AlerteService();
  @Provide('affectationService') private affectationService = () => new AffectationService();
  @Provide('grandeurService') private grandeurService = () => new GrandeurService();
  @Provide('plantageService') private plantageService = () => new PlantageService();
  @Provide('planteService') private planteService = () => new PlanteService();
  @Provide('typePlantService') private typePlantService = () => new TypePlantService();
  @Provide('boitierService') private boitierService = () => new BoitierService();
  @Provide('bracheService') private bracheService = () => new BracheService();
  @Provide('capteurService') private capteurService = () => new CapteurService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
