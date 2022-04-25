import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBoitier } from '@/shared/model/boitier.model';

import BoitierService from './boitier.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Boitier extends Vue {
  @Inject('boitierService') private boitierService: () => BoitierService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public boitiers: IBoitier[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBoitiers();
  }

  public clear(): void {
    this.retrieveAllBoitiers();
  }

  public retrieveAllBoitiers(): void {
    this.isFetching = true;
    this.boitierService()
      .retrieve()
      .then(
        res => {
          this.boitiers = res.data;
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

  public prepareRemove(instance: IBoitier): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeBoitier(): void {
    this.boitierService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.boitier.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllBoitiers();
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
