/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PlantageComponent from '@/entities/plantage/plantage.vue';
import PlantageClass from '@/entities/plantage/plantage.component';
import PlantageService from '@/entities/plantage/plantage.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Plantage Management Component', () => {
    let wrapper: Wrapper<PlantageClass>;
    let comp: PlantageClass;
    let plantageServiceStub: SinonStubbedInstance<PlantageService>;

    beforeEach(() => {
      plantageServiceStub = sinon.createStubInstance<PlantageService>(PlantageService);
      plantageServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PlantageClass>(PlantageComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          plantageService: () => plantageServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      plantageServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllPlantages();
      await comp.$nextTick();

      // THEN
      expect(plantageServiceStub.retrieve.called).toBeTruthy();
      expect(comp.plantages[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      plantageServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(plantageServiceStub.retrieve.callCount).toEqual(1);

      comp.removePlantage();
      await comp.$nextTick();

      // THEN
      expect(plantageServiceStub.delete.called).toBeTruthy();
      expect(plantageServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
