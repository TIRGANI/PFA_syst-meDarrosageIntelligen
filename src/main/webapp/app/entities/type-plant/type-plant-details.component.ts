import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITypePlant } from '@/shared/model/type-plant.model';
import TypePlantService from './type-plant.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TypePlantDetails extends Vue {
  @Inject('typePlantService') private typePlantService: () => TypePlantService;
  @Inject('alertService') private alertService: () => AlertService;

  public typePlant: ITypePlant = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.typePlantId) {
        vm.retrieveTypePlant(to.params.typePlantId);
      }
    });
  }

  public retrieveTypePlant(typePlantId) {
    this.typePlantService()
      .find(typePlantId)
      .then(res => {
        this.typePlant = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
