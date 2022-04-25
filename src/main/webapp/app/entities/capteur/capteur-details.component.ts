import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICapteur } from '@/shared/model/capteur.model';
import CapteurService from './capteur.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CapteurDetails extends Vue {
  @Inject('capteurService') private capteurService: () => CapteurService;
  @Inject('alertService') private alertService: () => AlertService;

  public capteur: ICapteur = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.capteurId) {
        vm.retrieveCapteur(to.params.capteurId);
      }
    });
  }

  public retrieveCapteur(capteurId) {
    this.capteurService()
      .find(capteurId)
      .then(res => {
        this.capteur = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
