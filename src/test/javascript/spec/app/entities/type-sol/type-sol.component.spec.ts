/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TypeSolComponent from '@/entities/type-sol/type-sol.vue';
import TypeSolClass from '@/entities/type-sol/type-sol.component';
import TypeSolService from '@/entities/type-sol/type-sol.service';
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
  describe('TypeSol Management Component', () => {
    let wrapper: Wrapper<TypeSolClass>;
    let comp: TypeSolClass;
    let typeSolServiceStub: SinonStubbedInstance<TypeSolService>;

    beforeEach(() => {
      typeSolServiceStub = sinon.createStubInstance<TypeSolService>(TypeSolService);
      typeSolServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TypeSolClass>(TypeSolComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          typeSolService: () => typeSolServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      typeSolServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTypeSols();
      await comp.$nextTick();

      // THEN
      expect(typeSolServiceStub.retrieve.called).toBeTruthy();
      expect(comp.typeSols[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      typeSolServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(typeSolServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTypeSol();
      await comp.$nextTick();

      // THEN
      expect(typeSolServiceStub.delete.called).toBeTruthy();
      expect(typeSolServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
