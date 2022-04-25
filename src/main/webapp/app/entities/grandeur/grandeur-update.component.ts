import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ParcelleService from '@/entities/parcelle/parcelle.service';
import { IParcelle } from '@/shared/model/parcelle.model';

import { IGrandeur, Grandeur } from '@/shared/model/grandeur.model';
import GrandeurService from './grandeur.service';

const validations: any = {
  grandeur: {
    type: {
      required,
    },
    valeur: {
      required,
    },
    date: {},
  },
};

@Component({
  validations,
})
export default class GrandeurUpdate extends Vue {
  @Inject('grandeurService') private grandeurService: () => GrandeurService;
  @Inject('alertService') private alertService: () => AlertService;

  public grandeur: IGrandeur = new Grandeur();

  @Inject('parcelleService') private parcelleService: () => ParcelleService;

  public parcelles: IParcelle[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.grandeurId) {
        vm.retrieveGrandeur(to.params.grandeurId);
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
    if (this.grandeur.id) {
      this.grandeurService()
        .update(this.grandeur)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.grandeur.updated', { param: param.id });
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
      this.grandeurService()
        .create(this.grandeur)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.grandeur.created', { param: param.id });
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

  public retrieveGrandeur(grandeurId): void {
    this.grandeurService()
      .find(grandeurId)
      .then(res => {
        this.grandeur = res;
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
