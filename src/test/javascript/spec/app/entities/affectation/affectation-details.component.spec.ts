/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AffectationDetailComponent from '@/entities/affectation/affectation-details.vue';
import AffectationClass from '@/entities/affectation/affectation-details.component';
import AffectationService from '@/entities/affectation/affectation.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Affectation Management Detail Component', () => {
    let wrapper: Wrapper<AffectationClass>;
    let comp: AffectationClass;
    let affectationServiceStub: SinonStubbedInstance<AffectationService>;

    beforeEach(() => {
      affectationServiceStub = sinon.createStubInstance<AffectationService>(AffectationService);

      wrapper = shallowMount<AffectationClass>(AffectationDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { affectationService: () => affectationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAffectation = { id: 123 };
        affectationServiceStub.find.resolves(foundAffectation);

        // WHEN
        comp.retrieveAffectation(123);
        await comp.$nextTick();

        // THEN
        expect(comp.affectation).toBe(foundAffectation);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAffectation = { id: 123 };
        affectationServiceStub.find.resolves(foundAffectation);

        // WHEN
        comp.beforeRouteEnter({ params: { affectationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.affectation).toBe(foundAffectation);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
