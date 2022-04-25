import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAffectation } from '@/shared/model/affectation.model';
import AffectationService from './affectation.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AffectationDetails extends Vue {
  @Inject('affectationService') private affectationService: () => AffectationService;
  @Inject('alertService') private alertService: () => AlertService;

  public affectation: IAffectation = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.affectationId) {
        vm.retrieveAffectation(to.params.affectationId);
      }
    });
  }

  public retrieveAffectation(affectationId) {
    this.affectationService()
      .find(affectationId)
      .then(res => {
        this.affectation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
