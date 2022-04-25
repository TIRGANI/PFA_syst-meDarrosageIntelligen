import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import HistoriqueService from '@/entities/historique/historique.service';
import { IHistorique } from '@/shared/model/historique.model';

import GrandeurService from '@/entities/grandeur/grandeur.service';
import { IGrandeur } from '@/shared/model/grandeur.model';

import AffectationService from '@/entities/affectation/affectation.service';
import { IAffectation } from '@/shared/model/affectation.model';

import FermService from '@/entities/ferm/ferm.service';
import { IFerm } from '@/shared/model/ferm.model';

import PlantageService from '@/entities/plantage/plantage.service';
import { IPlantage } from '@/shared/model/plantage.model';

import TypeSolService from '@/entities/type-sol/type-sol.service';
import { ITypeSol } from '@/shared/model/type-sol.model';

import AlerteService from '@/entities/alerte/alerte.service';
import { IAlerte } from '@/shared/model/alerte.model';

import { IParcelle, Parcelle } from '@/shared/model/parcelle.model';
import ParcelleService from './parcelle.service';

const validations: any = {
  parcelle: {
    surface: {},
    photo: {},
  },
};

@Component({
  validations,
})
export default class ParcelleUpdate extends Vue {
  @Inject('parcelleService') private parcelleService: () => ParcelleService;
  @Inject('alertService') private alertService: () => AlertService;

  public parcelle: IParcelle = new Parcelle();

  @Inject('historiqueService') private historiqueService: () => HistoriqueService;

  public historiques: IHistorique[] = [];

  @Inject('grandeurService') private grandeurService: () => GrandeurService;

  public grandeurs: IGrandeur[] = [];

  @Inject('affectationService') private affectationService: () => AffectationService;

  public affectations: IAffectation[] = [];

  @Inject('fermService') private fermService: () => FermService;

  public ferms: IFerm[] = [];

  @Inject('plantageService') private plantageService: () => PlantageService;

  public plantages: IPlantage[] = [];

  @Inject('typeSolService') private typeSolService: () => TypeSolService;

  public typeSols: ITypeSol[] = [];

  @Inject('alerteService') private alerteService: () => AlerteService;

  public alertes: IAlerte[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.parcelleId) {
        vm.retrieveParcelle(to.params.parcelleId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.parcelle.ferms = [];
    this.parcelle.plantages = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.parcelle.id) {
      this.parcelleService()
        .update(this.parcelle)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.parcelle.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.parcelleService()
        .create(this.parcelle)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.parcelle.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveParcelle(parcelleId): void {
    this.parcelleService()
      .find(parcelleId)
      .then(res => {
        this.parcelle = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.historiqueService()
      .retrieve()
      .then(res => {
        this.historiques = res.data;
      });
    this.grandeurService()
      .retrieve()
      .then(res => {
        this.grandeurs = res.data;
      });
    this.affectationService()
      .retrieve()
      .then(res => {
        this.affectations = res.data;
      });
    this.fermService()
      .retrieve()
      .then(res => {
        this.ferms = res.data;
      });
    this.plantageService()
      .retrieve()
      .then(res => {
        this.plantages = res.data;
      });
    this.typeSolService()
      .retrieve()
      .then(res => {
        this.typeSols = res.data;
      });
    this.alerteService()
      .retrieve()
      .then(res => {
        this.alertes = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
