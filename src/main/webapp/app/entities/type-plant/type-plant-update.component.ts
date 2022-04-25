import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import PlanteService from '@/entities/plante/plante.service';
import { IPlante } from '@/shared/model/plante.model';

import { ITypePlant, TypePlant } from '@/shared/model/type-plant.model';
import TypePlantService from './type-plant.service';

const validations: any = {
  typePlant: {
    lebelle: {
      required,
    },
    humiditeMax: {
      required,
      decimal,
    },
    humiditeMin: {
      required,
      decimal,
    },
    temperature: {
      required,
      decimal,
    },
    luminisite: {
      required,
      decimal,
    },
  },
};

@Component({
  validations,
})
export default class TypePlantUpdate extends Vue {
  @Inject('typePlantService') private typePlantService: () => TypePlantService;
  @Inject('alertService') private alertService: () => AlertService;

  public typePlant: ITypePlant = new TypePlant();

  @Inject('planteService') private planteService: () => PlanteService;

  public plantes: IPlante[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.typePlantId) {
        vm.retrieveTypePlant(to.params.typePlantId);
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
    if (this.typePlant.id) {
      this.typePlantService()
        .update(this.typePlant)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.typePlant.updated', { param: param.id });
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
      this.typePlantService()
        .create(this.typePlant)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.typePlant.created', { param: param.id });
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

  public retrieveTypePlant(typePlantId): void {
    this.typePlantService()
      .find(typePlantId)
      .then(res => {
        this.typePlant = res;
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
  }
}
