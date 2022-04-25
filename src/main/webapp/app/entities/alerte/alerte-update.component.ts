import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ParcelleService from '@/entities/parcelle/parcelle.service';
import { IParcelle } from '@/shared/model/parcelle.model';

import BoitierService from '@/entities/boitier/boitier.service';
import { IBoitier } from '@/shared/model/boitier.model';

import { IAlerte, Alerte } from '@/shared/model/alerte.model';
import AlerteService from './alerte.service';

const validations: any = {
  alerte: {
    humidite: {},
    temperature: {},
    luminosite: {},
  },
};

@Component({
  validations,
})
export default class AlerteUpdate extends Vue {
  @Inject('alerteService') private alerteService: () => AlerteService;
  @Inject('alertService') private alertService: () => AlertService;

  public alerte: IAlerte = new Alerte();

  @Inject('parcelleService') private parcelleService: () => ParcelleService;

  public parcelles: IParcelle[] = [];

  @Inject('boitierService') private boitierService: () => BoitierService;

  public boitiers: IBoitier[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.alerteId) {
        vm.retrieveAlerte(to.params.alerteId);
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
    if (this.alerte.id) {
      this.alerteService()
        .update(this.alerte)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.alerte.updated', { param: param.id });
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
      this.alerteService()
        .create(this.alerte)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.alerte.created', { param: param.id });
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

  public retrieveAlerte(alerteId): void {
    this.alerteService()
      .find(alerteId)
      .then(res => {
        this.alerte = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.parcelleService()
      .retrieve()
      .then(res => {
        this.parcelles = res.data;
      });
    this.boitierService()
      .retrieve()
      .then(res => {
        this.boitiers = res.data;
      });
  }
}
