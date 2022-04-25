import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ParcelleService from '@/entities/parcelle/parcelle.service';
import { IParcelle } from '@/shared/model/parcelle.model';

import { IFerm, Ferm } from '@/shared/model/ferm.model';
import FermService from './ferm.service';

const validations: any = {
  ferm: {
    numParcelle: {},
    photo: {},
  },
};

@Component({
  validations,
})
export default class FermUpdate extends Vue {
  @Inject('fermService') private fermService: () => FermService;
  @Inject('alertService') private alertService: () => AlertService;

  public ferm: IFerm = new Ferm();

  @Inject('parcelleService') private parcelleService: () => ParcelleService;

  public parcelles: IParcelle[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.fermId) {
        vm.retrieveFerm(to.params.fermId);
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
    if (this.ferm.id) {
      this.fermService()
        .update(this.ferm)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.ferm.updated', { param: param.id });
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
      this.fermService()
        .create(this.ferm)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.ferm.created', { param: param.id });
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

  public retrieveFerm(fermId): void {
    this.fermService()
      .find(fermId)
      .then(res => {
        this.ferm = res;
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
  }
}
