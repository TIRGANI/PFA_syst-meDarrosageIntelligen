import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICapteur } from '@/shared/model/capteur.model';

import CapteurService from './capteur.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Capteur extends Vue {
  @Inject('capteurService') private capteurService: () => CapteurService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public capteurs: ICapteur[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCapteurs();
  }

  public clear(): void {
    this.retrieveAllCapteurs();
  }

  public retrieveAllCapteurs(): void {
    this.isFetching = true;
    this.capteurService()
      .retrieve()
      .then(
        res => {
          this.capteurs = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: ICapteur): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCapteur(): void {
    this.capteurService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.capteur.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCapteurs();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
