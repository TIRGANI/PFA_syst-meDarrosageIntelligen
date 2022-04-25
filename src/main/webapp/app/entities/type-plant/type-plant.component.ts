import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITypePlant } from '@/shared/model/type-plant.model';

import TypePlantService from './type-plant.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class TypePlant extends Vue {
  @Inject('typePlantService') private typePlantService: () => TypePlantService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public typePlants: ITypePlant[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTypePlants();
  }

  public clear(): void {
    this.retrieveAllTypePlants();
  }

  public retrieveAllTypePlants(): void {
    this.isFetching = true;
    this.typePlantService()
      .retrieve()
      .then(
        res => {
          this.typePlants = res.data;
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

  public prepareRemove(instance: ITypePlant): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTypePlant(): void {
    this.typePlantService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.typePlant.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllTypePlants();
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
