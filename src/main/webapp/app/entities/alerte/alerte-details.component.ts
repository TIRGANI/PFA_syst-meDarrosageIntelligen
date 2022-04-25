import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAlerte } from '@/shared/model/alerte.model';
import AlerteService from './alerte.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AlerteDetails extends Vue {
  @Inject('alerteService') private alerteService: () => AlerteService;
  @Inject('alertService') private alertService: () => AlertService;

  public alerte: IAlerte = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.alerteId) {
        vm.retrieveAlerte(to.params.alerteId);
      }
    });
  }

  public retrieveAlerte(alerteId) {
    this.alerteService()
      .find(alerteId)
      .then(res => {
        this.alerte = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
