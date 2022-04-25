import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import BracheService from '@/entities/brache/brache.service';
import { IBrache } from '@/shared/model/brache.model';

import { ICapteur, Capteur } from '@/shared/model/capteur.model';
import CapteurService from './capteur.service';

const validations: any = {
  capteur: {
    type: {
      required,
    },
    image: {},
    description: {},
  },
};

@Component({
  validations,
})
export default class CapteurUpdate extends Vue {
  @Inject('capteurService') private capteurService: () => CapteurService;
  @Inject('alertService') private alertService: () => AlertService;

  public capteur: ICapteur = new Capteur();

  @Inject('bracheService') private bracheService: () => BracheService;

  public braches: IBrache[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.capteurId) {
        vm.retrieveCapteur(to.params.capteurId);
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
    if (this.capteur.id) {
      this.capteurService()
        .update(this.capteur)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.capteur.updated', { param: param.id });
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
      this.capteurService()
        .create(this.capteur)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.capteur.created', { param: param.id });
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

  public retrieveCapteur(capteurId): void {
    this.capteurService()
      .find(capteurId)
      .then(res => {
        this.capteur = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.bracheService()
      .retrieve()
      .then(res => {
        this.braches = res.data;
      });
  }
}
