import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IAffectation } from '@/shared/model/affectation.model';

import AffectationService from './affectation.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Affectation extends Vue {
  @Inject('affectationService') private affectationService: () => AffectationService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public affectations: IAffectation[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllAffectations();
  }

  public clear(): void {
    this.retrieveAllAffectations();
  }

  public retrieveAllAffectations(): void {
    this.isFetching = true;
    this.affectationService()
      .retrieve()
      .then(
        res => {
          this.affectations = res.data;
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

  public prepareRemove(instance: IAffectation): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAffectation(): void {
    this.affectationService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('pfaApp.affectation.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllAffectations();
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
