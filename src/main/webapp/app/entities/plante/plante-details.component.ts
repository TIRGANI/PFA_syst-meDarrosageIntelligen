import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPlante } from '@/shared/model/plante.model';
import PlanteService from './plante.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PlanteDetails extends Vue {
  @Inject('planteService') private planteService: () => PlanteService;
  @Inject('alertService') private alertService: () => AlertService;

  public plante: IPlante = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.planteId) {
        vm.retrievePlante(to.params.planteId);
      }
    });
  }

  public retrievePlante(planteId) {
    this.planteService()
      .find(planteId)
      .then(res => {
        this.plante = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
