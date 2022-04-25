import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPlantage } from '@/shared/model/plantage.model';

import PlantageService from './plantage.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Plantage extends Vue {
  @Inject('plantageService') private plantageService: () => PlantageService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public plantages: IPlantage[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPlantages();
  }

  public clear(): void {
    this.retrieveAllPlantages();
  }

  public retrieveAllPlantages(): void {
    this.isFetching = true;
    this.plantageService()
      .retrieve()
      .then(
        res => {
          this.plantages = res.data;
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

  public prepareRemove(instance: IPlantage): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePlantage(): void {
    this.plantageService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.plantage.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPlantages();
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
