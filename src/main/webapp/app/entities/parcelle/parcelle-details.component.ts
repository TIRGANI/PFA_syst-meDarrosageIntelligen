import { Component, Vue, Inject } from 'vue-property-decorator';

import { IParcelle } from '@/shared/model/parcelle.model';
import ParcelleService from './parcelle.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ParcelleDetails extends Vue {
  @Inject('parcelleService') private parcelleService: () => ParcelleService;
  @Inject('alertService') private alertService: () => AlertService;

  public parcelle: IParcelle = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.parcelleId) {
        vm.retrieveParcelle(to.params.parcelleId);
      }
    });
  }

  public retrieveParcelle(parcelleId) {
    this.parcelleService()
      .find(parcelleId)
      .then(res => {
        this.parcelle = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
