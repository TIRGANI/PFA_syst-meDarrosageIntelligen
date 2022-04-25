import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import AlerteService from '@/entities/alerte/alerte.service';
import { IAlerte } from '@/shared/model/alerte.model';

import BracheService from '@/entities/brache/brache.service';
import { IBrache } from '@/shared/model/brache.model';

import AffectationService from '@/entities/affectation/affectation.service';
import { IAffectation } from '@/shared/model/affectation.model';

import { IBoitier, Boitier } from '@/shared/model/boitier.model';
import BoitierService from './boitier.service';

const validations: any = {
  boitier: {
    reference: {},
    type: {},
  },
};

@Component({
  validations,
})
export default class BoitierUpdate extends Vue {
  @Inject('boitierService') private boitierService: () => BoitierService;
  @Inject('alertService') private alertService: () => AlertService;

  public boitier: IBoitier = new Boitier();

  @Inject('alerteService') private alerteService: () => AlerteService;

  public alertes: IAlerte[] = [];

  @Inject('bracheService') private bracheService: () => BracheService;

  public braches: IBrache[] = [];

  @Inject('affectationService') private affectationService: () => AffectationService;

  public affectations: IAffectation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.boitierId) {
        vm.retrieveBoitier(to.params.boitierId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.boitier.braches = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.boitier.id) {
      this.boitierService()
        .update(this.boitier)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.boitier.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.boitierService()
        .create(this.boitier)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pfaApp.boitier.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveBoitier(boitierId): void {
    this.boitierService()
      .find(boitierId)
      .then(res => {
        this.boitier = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.alerteService()
      .retrieve()
      .then(res => {
        this.alertes = res.data;
      });
    this.bracheService()
      .retrieve()
      .then(res => {
        this.braches = res.data;
      });
    this.affectationService()
      .retrieve()
      .then(res => {
        this.affectations = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
