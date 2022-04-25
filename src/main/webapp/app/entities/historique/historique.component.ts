import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IHistorique } from '@/shared/model/historique.model';

import HistoriqueService from './historique.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Historique extends Vue {
  @Inject('historiqueService') private historiqueService: () => HistoriqueService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public historiques: IHistorique[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllHistoriques();
  }

  public clear(): void {
    this.retrieveAllHistoriques();
  }

  public retrieveAllHistoriques(): void {
    this.isFetching = true;
    this.historiqueService()
      .retrieve()
      .then(
        res => {
          this.historiques = res.data;
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

  public prepareRemove(instance: IHistorique): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeHistorique(): void {
    this.historiqueService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.historique.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllHistoriques();
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
