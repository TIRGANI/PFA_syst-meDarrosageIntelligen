import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITypeSol } from '@/shared/model/type-sol.model';
import TypeSolService from './type-sol.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TypeSolDetails extends Vue {
  @Inject('typeSolService') private typeSolService: () => TypeSolService;
  @Inject('alertService') private alertService: () => AlertService;

  public typeSol: ITypeSol = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.typeSolId) {
        vm.retrieveTypeSol(to.params.typeSolId);
      }
    });
  }

  public retrieveTypeSol(typeSolId) {
    this.typeSolService()
      .find(typeSolId)
      .then(res => {
        this.typeSol = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
