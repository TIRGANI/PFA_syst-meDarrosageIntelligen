import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBrache } from '@/shared/model/brache.model';

import BracheService from './brache.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Brache extends Vue {
  @Inject('bracheService') private bracheService: () => BracheService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public braches: IBrache[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBraches();
  }

  public clear(): void {
    this.retrieveAllBraches();
  }

  public retrieveAllBraches(): void {
    this.isFetching = true;
    this.bracheService()
      .retrieve()
      .then(
        res => {
          this.braches = res.data;
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

  public prepareRemove(instance: IBrache): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeBrache(): void {
    this.bracheService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.brache.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllBraches();
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
