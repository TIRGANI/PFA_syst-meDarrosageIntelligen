import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IParcelle } from '@/shared/model/parcelle.model';

import ParcelleService from './parcelle.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Parcelle extends Vue {
  @Inject('parcelleService') private parcelleService: () => ParcelleService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public parcelles: IParcelle[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllParcelles();
  }

  public clear(): void {
    this.retrieveAllParcelles();
  }

  public retrieveAllParcelles(): void {
    this.isFetching = true;
    this.parcelleService()
      .retrieve()
      .then(
        res => {
          this.parcelles = res.data;
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

  public prepareRemove(instance: IParcelle): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeParcelle(): void {
    this.parcelleService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.parcelle.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllParcelles();
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
