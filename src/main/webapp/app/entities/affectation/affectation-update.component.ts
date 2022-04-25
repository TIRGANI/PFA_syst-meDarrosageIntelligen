import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import BoitierService from '@/entities/boitier/boitier.service';
import { IBoitier } from '@/shared/model/boitier.model';

import ParcelleService from '@/entities/parcelle/parcelle.service';
import { IParcelle } from '@/shared/model/parcelle.model';

import { IAffectation, Affectation } from '@/shared/model/affectation.model';
import AffectationService from './affectation.service';

const validations: any = {
  affectation: {
    dateDebut: {},
    dateFin: {},
  },
};

@Component({
  validations,
})
export default class AffectationUpdate extends Vue {
  @Inject('affectationService') private affectationService: () => AffectationService;
  @Inject('alertService') private alertService: () => AlertService;

  public affectation: IAffectation = new Affectation();

  @Inject('boitierService') private boitierService: () => BoitierService;

  public boitiers: IBoitier[] = [];

  @Inject('parcelleService') private parcelleService: () => ParcelleService;

  public parcelles: IParcelle[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.affectationId) {
        vm.retrieveAffectation(to.params.affectationId);
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
    if (this.affectation.id) {
      this.affectationService()
        .update(this.affectation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.affectation.updated', { param: param.id });
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
      this.affectationService()
        .create(this.affectation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.affectation.created', { param: param.id });
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

  public retrieveAffectation(affectationId): void {
    this.affectationService()
      .find(affectationId)
      .then(res => {
        this.affectation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.boitierService()
      .retrieve()
      .then(res => {
        this.boitiers = res.data;
      });
    this.parcelleService()
      .retrieve()
      .then(res => {
        this.parcelles = res.data;
      });
  }
}
