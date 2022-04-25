/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TypePlantComponent from '@/entities/type-plant/type-plant.vue';
import TypePlantClass from '@/entities/type-plant/type-plant.component';
import TypePlantService from '@/entities/type-plant/type-plant.service';
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
  describe('TypePlant Management Component', () => {
    let wrapper: Wrapper<TypePlantClass>;
    let comp: TypePlantClass;
    let typePlantServiceStub: SinonStubbedInstance<TypePlantService>;

    beforeEach(() => {
      typePlantServiceStub = sinon.createStubInstance<TypePlantService>(TypePlantService);
      typePlantServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TypePlantClass>(TypePlantComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          typePlantService: () => typePlantServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      typePlantServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTypePlants();
      await comp.$nextTick();

      // THEN
      expect(typePlantServiceStub.retrieve.called).toBeTruthy();
      expect(comp.typePlants[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      typePlantServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(typePlantServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTypePlant();
      await comp.$nextTick();

      // THEN
      expect(typePlantServiceStub.delete.called).toBeTruthy();
      expect(typePlantServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
