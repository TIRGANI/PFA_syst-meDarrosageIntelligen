import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBoitier } from '@/shared/model/boitier.model';
import BoitierService from './boitier.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class BoitierDetails extends Vue {
  @Inject('boitierService') private boitierService: () => BoitierService;
  @Inject('alertService') private alertService: () => AlertService;

  public boitier: IBoitier = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.boitierId) {
        vm.retrieveBoitier(to.params.boitierId);
      }
    });
  }

  public retrieveBoitier(boitierId) {
    this.boitierService()
      .find(boitierId)
      .then(res => {
        this.boitier = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
