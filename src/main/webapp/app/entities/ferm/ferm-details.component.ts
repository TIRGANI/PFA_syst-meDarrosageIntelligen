import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFerm } from '@/shared/model/ferm.model';
import FermService from './ferm.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FermDetails extends Vue {
  @Inject('fermService') private fermService: () => FermService;
  @Inject('alertService') private alertService: () => AlertService;

  public ferm: IFerm = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.fermId) {
        vm.retrieveFerm(to.params.fermId);
      }
    });
  }

  public retrieveFerm(fermId) {
    this.fermService()
      .find(fermId)
      .then(res => {
        this.ferm = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
