<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="capteur">
        <h2 class="jh-entity-heading" data-cy="capteurDetailsHeading">
          <span v-text="$t('pfaApp.capteur.detail.title')">Capteur</span> {{ capteur.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.capteur.type')">Type</span>
          </dt>
          <dd>
            <span>{{ capteur.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.capteur.image')">Image</span>
          </dt>
          <dd>
            <span>{{ capteur.image }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.capteur.description')">Description</span>
          </dt>
          <dd>
            <span>{{ capteur.description }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="capteur.id" :to="{ name: 'CapteurEdit', params: { capteurId: capteur.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./capteur-details.component.ts"></script>
