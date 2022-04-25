import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFerm } from '@/shared/model/ferm.model';

import FermService from './ferm.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Ferm extends Vue {
  @Inject('fermService') private fermService: () => FermService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public ferms: IFerm[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFerms();
  }

  public clear(): void {
    this.retrieveAllFerms();
  }

  public retrieveAllFerms(): void {
    this.isFetching = true;
    this.fermService()
      .retrieve()
      .then(
        res => {
          this.ferms = res.data;
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

  public prepareRemove(instance: IFerm): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFerm(): void {
    this.fermService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.ferm.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFerms();
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
