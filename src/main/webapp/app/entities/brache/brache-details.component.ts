import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBrache } from '@/shared/model/brache.model';
import BracheService from './brache.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class BracheDetails extends Vue {
  @Inject('bracheService') private bracheService: () => BracheService;
  @Inject('alertService') private alertService: () => AlertService;

  public brache: IBrache = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.bracheId) {
        vm.retrieveBrache(to.params.bracheId);
      }
    });
  }

  public retrieveBrache(bracheId) {
    this.bracheService()
      .find(bracheId)
      .then(res => {
        this.brache = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
