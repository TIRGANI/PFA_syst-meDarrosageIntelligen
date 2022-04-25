import { Component, Vue, Inject } from 'vue-property-decorator';

import { IHistorique } from '@/shared/model/historique.model';
import HistoriqueService from './historique.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class HistoriqueDetails extends Vue {
  @Inject('historiqueService') private historiqueService: () => HistoriqueService;
  @Inject('alertService') private alertService: () => AlertService;

  public historique: IHistorique = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.historiqueId) {
        vm.retrieveHistorique(to.params.historiqueId);
      }
    });
  }

  public retrieveHistorique(historiqueId) {
    this.historiqueService()
      .find(historiqueId)
      .then(res => {
        this.historique = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
