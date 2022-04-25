import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IGrandeur } from '@/shared/model/grandeur.model';

import GrandeurService from './grandeur.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Grandeur extends Vue {
  @Inject('grandeurService') private grandeurService: () => GrandeurService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public grandeurs: IGrandeur[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllGrandeurs();
  }

  public clear(): void {
    this.retrieveAllGrandeurs();
  }

  public retrieveAllGrandeurs(): void {
    this.isFetching = true;
    this.grandeurService()
      .retrieve()
      .then(
        res => {
          this.grandeurs = res.data;
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

  public prepareRemove(instance: IGrandeur): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeGrandeur(): void {
    this.grandeurService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.grandeur.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllGrandeurs();
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
