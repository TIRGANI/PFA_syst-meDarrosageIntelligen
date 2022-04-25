/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AffectationComponent from '@/entities/affectation/affectation.vue';
import AffectationClass from '@/entities/affectation/affectation.component';
import AffectationService from '@/entities/affectation/affectation.service';
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
  describe('Affectation Management Component', () => {
    let wrapper: Wrapper<AffectationClass>;
    let comp: AffectationClass;
    let affectationServiceStub: SinonStubbedInstance<AffectationService>;

    beforeEach(() => {
      affectationServiceStub = sinon.createStubInstance<AffectationService>(AffectationService);
      affectationServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AffectationClass>(AffectationComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          affectationService: () => affectationServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      affectationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAffectations();
      await comp.$nextTick();

      // THEN
      expect(affectationServiceStub.retrieve.called).toBeTruthy();
      expect(comp.affectations[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      affectationServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(affectationServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAffectation();
      await comp.$nextTick();

      // THEN
      expect(affectationServiceStub.delete.called).toBeTruthy();
      expect(affectationServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
