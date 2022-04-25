import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITypeSol } from '@/shared/model/type-sol.model';

import TypeSolService from './type-sol.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class TypeSol extends Vue {
  @Inject('typeSolService') private typeSolService: () => TypeSolService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public typeSols: ITypeSol[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTypeSols();
  }

  public clear(): void {
    this.retrieveAllTypeSols();
  }

  public retrieveAllTypeSols(): void {
    this.isFetching = true;
    this.typeSolService()
      .retrieve()
      .then(
        res => {
          this.typeSols = res.data;
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

  public prepareRemove(instance: ITypeSol): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTypeSol(): void {
    this.typeSolService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.typeSol.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllTypeSols();
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
