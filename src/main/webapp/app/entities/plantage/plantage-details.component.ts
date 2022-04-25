import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPlantage } from '@/shared/model/plantage.model';
import PlantageService from './plantage.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PlantageDetails extends Vue {
  @Inject('plantageService') private plantageService: () => PlantageService;
  @Inject('alertService') private alertService: () => AlertService;

  public plantage: IPlantage = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.plantageId) {
        vm.retrievePlantage(to.params.plantageId);
      }
    });
  }

  public retrievePlantage(plantageId) {
    this.plantageService()
      .find(plantageId)
      .then(res => {
        this.plantage = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
