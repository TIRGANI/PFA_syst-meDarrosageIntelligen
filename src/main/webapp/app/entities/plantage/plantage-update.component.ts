import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import PlanteService from '@/entities/plante/plante.service';
import { IPlante } from '@/shared/model/plante.model';

import ParcelleService from '@/entities/parcelle/parcelle.service';
import { IParcelle } from '@/shared/model/parcelle.model';

import { IPlantage, Plantage } from '@/shared/model/plantage.model';
import PlantageService from './plantage.service';

const validations: any = {
  plantage: {
    date: {},
    nbrPlate: {},
  },
};

@Component({
  validations,
})
export default class PlantageUpdate extends Vue {
  @Inject('plantageService') private plantageService: () => PlantageService;
  @Inject('alertService') private alertService: () => AlertService;

  public plantage: IPlantage = new Plantage();

  @Inject('planteService') private planteService: () => PlanteService;

  public plantes: IPlante[] = [];

  @Inject('parcelleService') private parcelleService: () => ParcelleService;

  public parcelles: IParcelle[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.plantageId) {
        vm.retrievePlantage(to.params.plantageId);
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
    this.plantage.plantes = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.plantage.id) {
      this.plantageService()
        .update(this.plantage)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.plantage.updated', { param: param.id });
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
      this.plantageService()
        .create(this.plantage)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.plantage.created', { param: param.id });
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

  public retrievePlantage(plantageId): void {
    this.plantageService()
      .find(plantageId)
      .then(res => {
        this.plantage = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.planteService()
      .retrieve()
      .then(res => {
        this.plantes = res.data;
      });
    this.parcelleService()
      .retrieve()
      .then(res => {
        this.parcelles = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
