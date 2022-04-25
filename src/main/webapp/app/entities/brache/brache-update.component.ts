import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import CapteurService from '@/entities/capteur/capteur.service';
import { ICapteur } from '@/shared/model/capteur.model';

import BoitierService from '@/entities/boitier/boitier.service';
import { IBoitier } from '@/shared/model/boitier.model';

import { IBrache, Brache } from '@/shared/model/brache.model';
import BracheService from './brache.service';

const validations: any = {
  brache: {
    branche: {
      required,
      numeric,
    },
  },
};

@Component({
  validations,
})
export default class BracheUpdate extends Vue {
  @Inject('bracheService') private bracheService: () => BracheService;
  @Inject('alertService') private alertService: () => AlertService;

  public brache: IBrache = new Brache();

  @Inject('capteurService') private capteurService: () => CapteurService;

  public capteurs: ICapteur[] = [];

  @Inject('boitierService') private boitierService: () => BoitierService;

  public boitiers: IBoitier[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.bracheId) {
        vm.retrieveBrache(to.params.bracheId);
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
    this.brache.capteurs = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.brache.id) {
      this.bracheService()
        .update(this.brache)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.brache.updated', { param: param.id });
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
      this.bracheService()
        .create(this.brache)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.brache.created', { param: param.id });
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

  public retrieveBrache(bracheId): void {
    this.bracheService()
      .find(bracheId)
      .then(res => {
        this.brache = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.capteurService()
      .retrieve()
      .then(res => {
        this.capteurs = res.data;
      });
    this.boitierService()
      .retrieve()
      .then(res => {
        this.boitiers = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
