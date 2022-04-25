import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TypePlantService from '@/entities/type-plant/type-plant.service';
import { ITypePlant } from '@/shared/model/type-plant.model';

import PlantageService from '@/entities/plantage/plantage.service';
import { IPlantage } from '@/shared/model/plantage.model';

import { IPlante, Plante } from '@/shared/model/plante.model';
import PlanteService from './plante.service';

const validations: any = {
  plante: {
    lebelle: {
      required,
    },
    photo: {},
    racin: {},
  },
};

@Component({
  validations,
})
export default class PlanteUpdate extends Vue {
  @Inject('planteService') private planteService: () => PlanteService;
  @Inject('alertService') private alertService: () => AlertService;

  public plante: IPlante = new Plante();

  @Inject('typePlantService') private typePlantService: () => TypePlantService;

  public typePlants: ITypePlant[] = [];

  @Inject('plantageService') private plantageService: () => PlantageService;

  public plantages: IPlantage[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.planteId) {
        vm.retrievePlante(to.params.planteId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.plante.id) {
      this.planteService()
        .update(this.plante)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.plante.updated', { param: param.id });
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
      this.planteService()
        .create(this.plante)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.plante.created', { param: param.id });
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

  public retrievePlante(planteId): void {
    this.planteService()
      .find(planteId)
      .then(res => {
        this.plante = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.typePlantService()
      .retrieve()
      .then(res => {
        this.typePlants = res.data;
      });
    this.plantageService()
      .retrieve()
      .then(res => {
        this.plantages = res.data;
      });
  }
}
