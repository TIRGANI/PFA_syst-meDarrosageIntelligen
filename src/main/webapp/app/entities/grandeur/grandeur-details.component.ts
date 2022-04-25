import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGrandeur } from '@/shared/model/grandeur.model';
import GrandeurService from './grandeur.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class GrandeurDetails extends Vue {
  @Inject('grandeurService') private grandeurService: () => GrandeurService;
  @Inject('alertService') private alertService: () => AlertService;

  public grandeur: IGrandeur = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.grandeurId) {
        vm.retrieveGrandeur(to.params.grandeurId);
      }
    });
  }

  public retrieveGrandeur(grandeurId) {
    this.grandeurService()
      .find(grandeurId)
      .then(res => {
        this.grandeur = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
