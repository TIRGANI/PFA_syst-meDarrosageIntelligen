<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="brache">
        <h2 class="jh-entity-heading" data-cy="bracheDetailsHeading">
          <span v-text="$t('pfaApp.brache.detail.title')">Brache</span> {{ brache.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.brache.branche')">Branche</span>
          </dt>
          <dd>
            <span>{{ brache.branche }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.brache.capteur')">Capteur</span>
          </dt>
          <dd>
            <span v-for="(capteur, i) in brache.capteurs" :key="capteur.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'CapteurView', params: { capteurId: capteur.id } }">{{ capteur.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="brache.id" :to="{ name: 'BracheEdit', params: { bracheId: brache.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./brache-details.component.ts"></script>
