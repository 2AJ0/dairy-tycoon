/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  androidx.compose.runtime.internal.StabilityInferred
 *  androidx.lifecycle.AndroidViewModel
 *  androidx.lifecycle.ViewModel
 *  androidx.lifecycle.ViewModelKt
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.Pair
 *  kotlin.ResultKt
 *  kotlin.TuplesKt
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.collections.MapsKt
 *  kotlin.collections.SetsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.coroutines.Continuation
 *  kotlin.coroutines.intrinsics.IntrinsicsKt
 *  kotlin.coroutines.jvm.internal.Boxing
 *  kotlin.coroutines.jvm.internal.SpillingKt
 *  kotlin.jvm.functions.Function2
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.Ref$BooleanRef
 *  kotlin.jvm.internal.Ref$ObjectRef
 *  kotlin.jvm.internal.SourceDebugExtension
 *  kotlin.jvm.internal.StringCompanionObject
 *  kotlin.random.Random
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  kotlinx.coroutines.BuildersKt
 *  kotlinx.coroutines.CoroutineScope
 *  kotlinx.coroutines.DelayKt
 *  kotlinx.coroutines.flow.FlowKt
 *  kotlinx.coroutines.flow.MutableStateFlow
 *  kotlinx.coroutines.flow.SharingStarted
 *  kotlinx.coroutines.flow.SharingStarted$Companion
 *  kotlinx.coroutines.flow.StateFlow
 *  kotlinx.coroutines.flow.StateFlowKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.example.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.compose.runtime.internal.StabilityInferred;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import com.example.data.SaveGameManager;
import com.example.data.SaveSummary;
import com.example.data.SettingsRepository;
import com.example.model.Achievement;
import com.example.model.ActiveProject;
import com.example.model.BankState;
import com.example.model.Building;
import com.example.model.BuildingType;
import com.example.model.ChatMessage;
import com.example.model.ColdStoragePriority;
import com.example.model.ContractOffer;
import com.example.model.DailyReport;
import com.example.model.DefaultBuildings;
import com.example.model.EndgameChoice;
import com.example.model.Executive;
import com.example.model.ExecutiveRole;
import com.example.model.GameAction;
import com.example.model.GamePhase;
import com.example.model.GameState;
import com.example.model.GuidanceState;
import com.example.model.InventoryBatch;
import com.example.model.InventoryMethod;
import com.example.model.LifetimeStats;
import com.example.model.MarketItemState;
import com.example.model.Mentor;
import com.example.model.MessageStatus;
import com.example.model.NetWorthPhase;
import com.example.model.NewsCatalog;
import com.example.model.NewsEvent;
import com.example.model.NodeStatus;
import com.example.model.PlayerSkills;
import com.example.model.ProcessingRecipe;
import com.example.model.Product;
import com.example.model.ProductCatalog;
import com.example.model.ProjectType;
import com.example.model.ResearchCatalog;
import com.example.model.ResearchCategory;
import com.example.model.ResearchNode;
import com.example.model.RivalCatalog;
import com.example.model.RivalCompany;
import com.example.model.SkillType;
import com.example.model.TechCatalog;
import com.example.model.TechTreeNode;
import com.example.model.UnlockedFeatures;
import com.example.ui.screens.DrawerDestination;
import com.example.viewmodel.GameViewModel;
import com.example.viewmodel.IntentParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.random.Random;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 2, 0}, k=1, xi=48, d1={"\u0000\u00ae\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fJ\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fJ\u0006\u0010>\u001a\u00020\u0012J\u0006\u0010?\u001a\u00020\u0012J\u0006\u0010@\u001a\u00020\u0012J\u0006\u0010A\u001a\u00020\u0012J\u0006\u0010B\u001a\u00020\u0012J\u0006\u0010C\u001a\u00020\u0012J\u0006\u0010D\u001a\u00020\u0012J\u000e\u0010E\u001a\u00020\u00122\u0006\u0010F\u001a\u00020\fJ\u000e\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020JJ\u000e\u0010K\u001a\u00020\u00172\u0006\u0010L\u001a\u00020\u0017J\u0018\u0010M\u001a\u00020N2\u0006\u0010L\u001a\u00020\u00172\u0006\u0010O\u001a\u000201H\u0002J\u0006\u0010P\u001a\u00020\u0012J\u000e\u0010Q\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u000e\u0010T\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u0016\u0010U\u001a\u00020\u00122\u0006\u0010V\u001a\u0002012\u0006\u0010W\u001a\u000201J\u000e\u0010X\u001a\u00020\u00122\u0006\u0010V\u001a\u000201J\u000e\u0010Y\u001a\u00020\u00122\u0006\u0010Z\u001a\u000201J\u000e\u0010[\u001a\u00020\u00122\u0006\u0010\\\u001a\u000201J\u000e\u0010]\u001a\u00020\u00122\u0006\u0010\\\u001a\u000201J\u000e\u0010^\u001a\u00020\u00122\u0006\u0010\\\u001a\u000201J\u000e\u0010_\u001a\u00020\u00122\u0006\u0010`\u001a\u00020aJ\u001a\u0010b\u001a\u0004\u0018\u00010c2\u0006\u0010d\u001a\u00020\u00172\u0006\u0010e\u001a\u00020cH\u0002J\u000e\u0010f\u001a\u00020\u00122\u0006\u0010g\u001a\u000201J\u0016\u0010h\u001a\u00020\u00122\u0006\u0010O\u001a\u0002012\u0006\u0010i\u001a\u00020\fJ\u0016\u0010j\u001a\u00020\u00122\u0006\u0010O\u001a\u0002012\u0006\u0010i\u001a\u00020\fJ\u0006\u0010k\u001a\u00020\u0012J\u0006\u0010l\u001a\u00020\u0012J\u0016\u0010m\u001a\u00020\u00122\u0006\u0010n\u001a\u0002012\u0006\u0010R\u001a\u00020NJ\u0016\u0010o\u001a\u00020\u00122\u0006\u0010p\u001a\u0002012\u0006\u0010q\u001a\u00020NJ\u000e\u0010r\u001a\u00020\u00122\u0006\u0010s\u001a\u000201J\u000e\u0010t\u001a\u00020\u00122\u0006\u0010u\u001a\u000201J\u000e\u0010v\u001a\u00020\u00122\u0006\u0010u\u001a\u000201J\u0006\u0010w\u001a\u00020\u0012J\u000e\u0010x\u001a\u00020\u00122\u0006\u0010y\u001a\u000201J\u000e\u0010z\u001a\u00020\u00122\u0006\u0010u\u001a\u000201J\u0016\u0010{\u001a\u00020\u00122\u0006\u0010u\u001a\u0002012\u0006\u0010|\u001a\u00020}J\u000e\u0010~\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u000e\u0010\u007f\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u000f\u0010\u0080\u0001\u001a\u00020\u00122\u0006\u0010u\u001a\u000201J\u0018\u0010\u0081\u0001\u001a\u00020\u00122\u0006\u0010u\u001a\u0002012\u0007\u0010\u0082\u0001\u001a\u00020NJ\u0018\u0010\u0083\u0001\u001a\u00020\u00122\u0006\u0010u\u001a\u0002012\u0007\u0010\u0084\u0001\u001a\u00020NJ\u0011\u0010\u0085\u0001\u001a\u00020\u00122\b\u0010\u0086\u0001\u001a\u00030\u0087\u0001J\u000f\u0010\u0088\u0001\u001a\u00020\u00122\u0006\u0010u\u001a\u000201J\u000f\u0010\u0089\u0001\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u000f\u0010\u008a\u0001\u001a\u00020\u00122\u0006\u0010R\u001a\u00020SJ\u0019\u0010\u008b\u0001\u001a\u00020\u00122\u0007\u0010\u008c\u0001\u001a\u0002012\u0007\u0010\u008d\u0001\u001a\u00020NJ\u0019\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008c\u0001\u001a\u0002012\u0007\u0010\u008d\u0001\u001a\u00020NJ\u0010\u0010\u008f\u0001\u001a\u00020\u00122\u0007\u0010\u008c\u0001\u001a\u000201J\u0010\u0010\u0090\u0001\u001a\u00020\u00122\u0007\u0010\u008c\u0001\u001a\u000201J\u0007\u0010\u0091\u0001\u001a\u00020\fJ\u0007\u0010\u0092\u0001\u001a\u00020\fJ\u0014\u0010\u0093\u0001\u001a\u00020\f2\u000b\b\u0002\u0010\u0094\u0001\u001a\u0004\u0018\u000101J\u0007\u0010\u0095\u0001\u001a\u00020\u0012J\u0010\u0010\u0096\u0001\u001a\u00020\u00122\u0007\u0010\u0094\u0001\u001a\u000201J\u0011\u0010\u0097\u0001\u001a\u00020\u00122\b\u0010\u0098\u0001\u001a\u00030\u0099\u0001J\u0011\u0010\u009a\u0001\u001a\u00020\u00122\b\u0010\u009b\u0001\u001a\u00030\u009c\u0001J\u0007\u0010\u009d\u0001\u001a\u00020\u0012J\u0007\u0010\u009e\u0001\u001a\u00020\u0012J\u0007\u0010\u009f\u0001\u001a\u00020\u0012J\u0007\u0010\u00a0\u0001\u001a\u00020\u0012J\u0007\u0010\u00a1\u0001\u001a\u00020\u0012J\u0011\u0010\u00a2\u0001\u001a\u00020\u00122\b\u0010\u00a3\u0001\u001a\u00030\u00a4\u0001J\u0018\u0010\u00a5\u0001\u001a\u00020\u00122\u0006\u0010O\u001a\u0002012\u0007\u0010\u00a6\u0001\u001a\u00020NJ\u0019\u0010\u00a7\u0001\u001a\u00020\u00172\u0006\u0010L\u001a\u00020\u00172\u0006\u0010g\u001a\u000201H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000eR\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u000eR\u0016\u0010\u001d\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001e0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001e0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u000eR\u0014\u0010!\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u000eR\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u000eR\u0014\u0010'\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u000eR\u0014\u0010*\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u000eR\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020\f0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u000eR\u0016\u00100\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u00102\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u000eR\u001d\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000206050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\u000eR\u001d\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000206050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\u000eR\u001d\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000206050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\u000eR\u001d\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000206050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u000e\u00a8\u0006\u00a8\u0001"}, d2={"Lcom/example/viewmodel/GameViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "saveGameManager", "Lcom/example/data/SaveGameManager;", "settingsRepository", "Lcom/example/data/SettingsRepository;", "soundEffectsEnabled", "Lkotlinx/coroutines/flow/StateFlow;", "", "getSoundEffectsEnabled", "()Lkotlinx/coroutines/flow/StateFlow;", "newsAlertsEnabled", "getNewsAlertsEnabled", "toggleSoundEffects", "", "enabled", "toggleNewsAlerts", "_gameState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/model/GameState;", "gameState", "getGameState", "_hasSavedGame", "hasSavedGame", "getHasSavedGame", "_saveSummary", "Lcom/example/data/SaveSummary;", "saveSummary", "getSaveSummary", "_showDailyReportDialog", "showDailyReportDialog", "getShowDailyReportDialog", "_showNewsChronicleDialog", "showNewsChronicleDialog", "getShowNewsChronicleDialog", "_showEndgameDialog", "showEndgameDialog", "getShowEndgameDialog", "_showMilestoneScreen", "showMilestoneScreen", "getShowMilestoneScreen", "_showCrossroadsDialog", "showCrossroadsDialog", "getShowCrossroadsDialog", "_snackBarMessage", "", "snackBarMessage", "getSnackBarMessage", "techNodesProducts", "", "Lcom/example/model/TechTreeNode;", "getTechNodesProducts", "techNodesIndustry", "getTechNodesIndustry", "techNodesCompany", "getTechNodesCompany", "techNodesPersonal", "getTechNodesPersonal", "clearSnackBar", "dismissDailyReport", "dismissNewsChronicle", "openNewsChronicle", "dismissEndgameDialog", "openEndgameDialog", "dismissCrossroadsDialog", "chooseCrossroadsOption", "incorporate", "evaluateAction", "Lcom/example/model/GuidanceState;", "action", "Lcom/example/model/GameAction;", "checkAchievements", "state", "calculateDailyUsage", "", "itemId", "endDay", "depositFunds", "amount", "", "withdrawFunds", "processPlayerMessage", "mentorId", "messageText", "sendApologyGift", "hireExecutive", "executiveId", "acceptContract", "offerId", "declineContract", "bargainContract", "upgradePlayerSkill", "skillType", "Lcom/example/model/SkillType;", "tryAssignProject", "Lcom/example/model/ActiveProject;", "current", "baseProject", "unlockTechnology", "techId", "toggleAutoBuy", "isActive", "toggleAutoSell", "workManualLabor", "studyManualResearch", "sellProduct", "productType", "sellInventoryBatch", "batchId", "quantityToSell", "sellAllOfProduct", "productId", "buyBuilding", "buildingId", "upgradeBuilding", "buyLandExpansion", "rushProject", "projectId", "toggleBuildingOperational", "setFacilityRecipe", "recipe", "Lcom/example/model/ProcessingRecipe;", "borrowLoan", "repayLoan", "constructOrUpgradeBuilding", "setBuildingRecipe", "recipeIndex", "updateFacilityAllocation", "percentage", "setInventoryMethod", "method", "Lcom/example/model/InventoryMethod;", "upgradeFactoryCapacity", "takeBankLoan", "repayBankLoan", "buyShares", "rivalId", "quantity", "sellShares", "launchCorporateSabotage", "launchSmearCampaign", "checkWinState", "saveGame", "loadGame", "saveId", "startNewGame", "deleteSave", "chooseEndgameOption", "choice", "Lcom/example/model/EndgameChoice;", "markFeatureAsSeen", "destination", "Lcom/example/ui/screens/DrawerDestination;", "discardSpoiledGoods", "restartGame", "goCorporate", "startSandbox", "retireSave", "updateColdStoragePriority", "priority", "Lcom/example/model/ColdStoragePriority;", "updateManualColdStorageAllocation", "allocation", "applyResearchEffect", "app"})
@StabilityInferred(parameters=0)
@SourceDebugExtension(value={"SMAP\nGameViewModel.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GameViewModel.kt\ncom/example/viewmodel/GameViewModel\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 StateFlow.kt\nkotlinx/coroutines/flow/StateFlowKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,2809:1\n774#2:2810\n865#2,2:2811\n774#2:2813\n865#2,2:2814\n774#2:2816\n865#2,2:2817\n774#2:2819\n865#2,2:2820\n1563#2:2833\n1634#2,2:2834\n1761#2,3:2836\n1761#2,3:2839\n1761#2,3:2842\n1788#2,4:2845\n774#2:2849\n865#2,2:2850\n774#2:2852\n865#2,2:2853\n1636#2:2855\n774#2:2856\n865#2,2:2857\n774#2:2859\n865#2,2:2860\n1563#2:2875\n1634#2,3:2876\n1563#2:2884\n1634#2,3:2885\n774#2:2893\n865#2,2:2894\n774#2:2901\n865#2,2:2902\n1563#2:2909\n1634#2,3:2910\n774#2:2913\n865#2,2:2914\n1788#2,4:2923\n1788#2,4:2927\n295#2,2:2934\n774#2:2936\n865#2,2:2937\n1617#2,9:2939\n1869#2:2948\n295#2,2:2949\n1870#2:2952\n1626#2:2953\n774#2:2979\n865#2,2:2980\n360#2,7:2987\n774#2:2999\n865#2,2:3000\n1869#2,2:3002\n774#2:3004\n865#2,2:3005\n295#2,2:3012\n295#2,2:3014\n295#2,2:3021\n1563#2:3033\n1634#2,3:3034\n1563#2:3037\n1634#2,3:3038\n827#2:3041\n855#2,2:3042\n1563#2:3049\n1634#2,3:3050\n1563#2:3058\n1634#2,3:3059\n295#2,2:3074\n1563#2:3079\n1634#2,3:3080\n1563#2:3093\n1634#2,3:3094\n774#2:3102\n865#2,2:3103\n1563#2:3115\n1634#2,3:3116\n1563#2:3124\n1634#2,3:3125\n1740#2,3:3130\n774#2:3151\n865#2,2:3152\n774#2:3154\n865#2,2:3155\n295#2,2:3184\n230#3,5:2822\n230#3,5:2827\n230#3,5:2862\n230#3,5:2867\n230#3,3:2872\n233#3,2:2879\n230#3,3:2881\n233#3,2:2888\n230#3,3:2890\n233#3,2:2896\n230#3,3:2898\n233#3,2:2904\n230#3,3:2906\n233#3,2:2916\n230#3,5:2918\n230#3,3:2931\n233#3,2:2954\n230#3,5:2956\n230#3,5:2961\n230#3,5:2966\n230#3,5:2971\n230#3,3:2976\n233#3,2:2982\n230#3,3:2984\n233#3,2:2994\n230#3,3:2996\n233#3,2:3007\n230#3,3:3009\n233#3,2:3016\n230#3,3:3018\n233#3,2:3023\n230#3,5:3025\n230#3,3:3030\n233#3,2:3044\n230#3,3:3046\n233#3,2:3053\n230#3,3:3055\n233#3,2:3062\n230#3,5:3064\n230#3,5:3069\n230#3,3:3076\n233#3,2:3083\n230#3,5:3085\n230#3,3:3090\n233#3,2:3097\n230#3,3:3099\n233#3,2:3105\n230#3,5:3107\n230#3,3:3112\n233#3,2:3119\n230#3,3:3121\n233#3,2:3128\n230#3,5:3133\n230#3,5:3138\n230#3,5:3143\n230#3,3:3148\n233#3,2:3157\n230#3,5:3159\n230#3,5:3164\n230#3,5:3169\n230#3,5:3174\n230#3,5:3179\n1#4:2832\n1#4:2951\n*S KotlinDebug\n*F\n+ 1 GameViewModel.kt\ncom/example/viewmodel/GameViewModel\n*L\n106#1:2810\n106#1:2811,2\n107#1:2813\n107#1:2814,2\n108#1:2816\n108#1:2817,2\n109#1:2819\n109#1:2820,2\n191#1:2833\n191#1:2834,2\n197#1:2836,3\n200#1:2839,3\n202#1:2842,3\n210#1:2845,4\n216#1:2849\n216#1:2850,2\n219#1:2852\n219#1:2853,2\n191#1:2855\n257#1:2856\n257#1:2857,2\n260#1:2859\n260#1:2860,2\n1472#1:2875\n1472#1:2876,3\n1490#1:2884\n1490#1:2885,3\n1520#1:2893\n1520#1:2894,2\n1543#1:2901\n1543#1:2902,2\n1577#1:2909\n1577#1:2910,3\n1590#1:2913\n1590#1:2914,2\n1649#1:2923,4\n1658#1:2927,4\n1682#1:2934,2\n1688#1:2936\n1688#1:2937,2\n1690#1:2939,9\n1690#1:2948\n1690#1:2949,2\n1690#1:2952\n1690#1:2953\n1823#1:2979\n1823#1:2980,2\n1889#1:2987,7\n1940#1:2999\n1940#1:3000,2\n1960#1:3002,2\n1965#1:3004\n1965#1:3005,2\n1990#1:3012,2\n2007#1:3014,2\n2055#1:3021,2\n2160#1:3033\n2160#1:3034,3\n2168#1:3037\n2168#1:3038,3\n2190#1:3041\n2190#1:3042,2\n2203#1:3049\n2203#1:3050,3\n2220#1:3058\n2220#1:3059,3\n2290#1:3074,2\n2307#1:3079\n2307#1:3080,3\n2351#1:3093\n2351#1:3094,3\n2401#1:3102\n2401#1:3103,2\n2519#1:3115\n2519#1:3116,3\n2568#1:3124\n2568#1:3125,3\n2601#1:3130,3\n2732#1:3151\n2732#1:3152,2\n2741#1:3154\n2741#1:3155,2\n2801#1:3184,2\n142#1:2822,5\n145#1:2827,5\n1315#1:2862,5\n1325#1:2867,5\n1449#1:2872,3\n1449#1:2879,2\n1481#1:2881,3\n1481#1:2888,2\n1509#1:2890,3\n1509#1:2896,2\n1540#1:2898,3\n1540#1:2904,2\n1556#1:2906,3\n1556#1:2916,2\n1607#1:2918,5\n1671#1:2931,3\n1671#1:2954,2\n1731#1:2956,5\n1745#1:2961,5\n1763#1:2966,5\n1793#1:2971,5\n1821#1:2976,3\n1821#1:2982,2\n1888#1:2984,3\n1888#1:2994,2\n1938#1:2996,3\n1938#1:3007,2\n1989#1:3009,3\n1989#1:3016,2\n2054#1:3018,3\n2054#1:3023,2\n2112#1:3025,5\n2141#1:3030,3\n2141#1:3044,2\n2202#1:3046,3\n2202#1:3053,2\n2219#1:3055,3\n2219#1:3062,2\n2236#1:3064,5\n2263#1:3069,5\n2306#1:3076,3\n2306#1:3083,2\n2314#1:3085,5\n2319#1:3090,3\n2319#1:3097,2\n2370#1:3099,3\n2370#1:3105,2\n2444#1:3107,5\n2486#1:3112,3\n2486#1:3119,2\n2536#1:3121,3\n2536#1:3128,2\n2605#1:3133,5\n2674#1:3138,5\n2710#1:3143,5\n2731#1:3148,3\n2731#1:3157,2\n2764#1:3159,5\n2773#1:3164,5\n2779#1:3169,5\n2784#1:3174,5\n2789#1:3179,5\n1690#1:2951\n*E\n"})
public final class GameViewModel
extends AndroidViewModel {
    @NotNull
    private final SaveGameManager saveGameManager;
    @NotNull
    private final SettingsRepository settingsRepository;
    @NotNull
    private final StateFlow<Boolean> soundEffectsEnabled;
    @NotNull
    private final StateFlow<Boolean> newsAlertsEnabled;
    @NotNull
    private final MutableStateFlow<GameState> _gameState;
    @NotNull
    private final StateFlow<GameState> gameState;
    @NotNull
    private final MutableStateFlow<Boolean> _hasSavedGame;
    @NotNull
    private final StateFlow<Boolean> hasSavedGame;
    @NotNull
    private final MutableStateFlow<SaveSummary> _saveSummary;
    @NotNull
    private final StateFlow<SaveSummary> saveSummary;
    @NotNull
    private final MutableStateFlow<Boolean> _showDailyReportDialog;
    @NotNull
    private final StateFlow<Boolean> showDailyReportDialog;
    @NotNull
    private final MutableStateFlow<Boolean> _showNewsChronicleDialog;
    @NotNull
    private final StateFlow<Boolean> showNewsChronicleDialog;
    @NotNull
    private final MutableStateFlow<Boolean> _showEndgameDialog;
    @NotNull
    private final StateFlow<Boolean> showEndgameDialog;
    @NotNull
    private final MutableStateFlow<Boolean> _showMilestoneScreen;
    @NotNull
    private final StateFlow<Boolean> showMilestoneScreen;
    @NotNull
    private final MutableStateFlow<Boolean> _showCrossroadsDialog;
    @NotNull
    private final StateFlow<Boolean> showCrossroadsDialog;
    @NotNull
    private final MutableStateFlow<String> _snackBarMessage;
    @NotNull
    private final StateFlow<String> snackBarMessage;
    @NotNull
    private final StateFlow<List<TechTreeNode>> techNodesProducts;
    @NotNull
    private final StateFlow<List<TechTreeNode>> techNodesIndustry;
    @NotNull
    private final StateFlow<List<TechTreeNode>> techNodesCompany;
    @NotNull
    private final StateFlow<List<TechTreeNode>> techNodesPersonal;
    public static final int $stable = 8;

    /*
     * WARNING - void declaration
     */
    public GameViewModel(@NotNull Application application) {
        void $this$filterTo\11;
        void $this$filter\10;
        void $this$filterTo\8;
        Iterable iterable;
        void $this$filterTo\5;
        Iterable iterable2;
        void $this$filterTo\2;
        Iterable iterable3;
        Intrinsics.checkNotNullParameter((Object)application, (String)"application");
        super(application);
        Context context = application.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue((Object)context, (String)"getApplicationContext(...)");
        this.saveGameManager = new SaveGameManager(context);
        Context context2 = application.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue((Object)context2, (String)"getApplicationContext(...)");
        this.settingsRepository = new SettingsRepository(context2);
        this.soundEffectsEnabled = FlowKt.stateIn(this.settingsRepository.getSoundEnabledFlow(), (CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), (SharingStarted)SharingStarted.Companion.WhileSubscribed$default((SharingStarted.Companion)SharingStarted.Companion, (long)5000L, (long)0L, (int)2, null), (Object)true);
        this.newsAlertsEnabled = FlowKt.stateIn(this.settingsRepository.getNotificationsEnabledFlow(), (CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), (SharingStarted)SharingStarted.Companion.WhileSubscribed$default((SharingStarted.Companion)SharingStarted.Companion, (long)5000L, (long)0L, (int)2, null), (Object)true);
        this._gameState = StateFlowKt.MutableStateFlow((Object)new GameState(null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262143, null));
        this.gameState = FlowKt.asStateFlow(this._gameState);
        this._hasSavedGame = StateFlowKt.MutableStateFlow((Object)this.saveGameManager.hasSavedGame());
        this.hasSavedGame = FlowKt.asStateFlow(this._hasSavedGame);
        this._saveSummary = StateFlowKt.MutableStateFlow((Object)SaveGameManager.getSaveSummary$default(this.saveGameManager, null, 1, null));
        this.saveSummary = FlowKt.asStateFlow(this._saveSummary);
        this._showDailyReportDialog = StateFlowKt.MutableStateFlow((Object)false);
        this.showDailyReportDialog = FlowKt.asStateFlow(this._showDailyReportDialog);
        this._showNewsChronicleDialog = StateFlowKt.MutableStateFlow((Object)false);
        this.showNewsChronicleDialog = FlowKt.asStateFlow(this._showNewsChronicleDialog);
        this._showEndgameDialog = StateFlowKt.MutableStateFlow((Object)false);
        this.showEndgameDialog = FlowKt.asStateFlow(this._showEndgameDialog);
        this._showMilestoneScreen = StateFlowKt.MutableStateFlow((Object)false);
        this.showMilestoneScreen = FlowKt.asStateFlow(this._showMilestoneScreen);
        this._showCrossroadsDialog = StateFlowKt.MutableStateFlow((Object)false);
        this.showCrossroadsDialog = FlowKt.asStateFlow(this._showCrossroadsDialog);
        this._snackBarMessage = StateFlowKt.MutableStateFlow(null);
        this.snackBarMessage = FlowKt.asStateFlow(this._snackBarMessage);
        Iterable iterable4 = TechCatalog.INSTANCE.getALL_TECHS();
        GameViewModel gameViewModel = this;
        boolean bl = false;
        void var4_5 = iterable3;
        Collection collection = new ArrayList();
        boolean bl2 = false;
        for (Object t : $this$filterTo\2) {
            TechTreeNode techTreeNode = (TechTreeNode)t;
            boolean bl3 = false;
            if (!(techTreeNode.getCategory() == ResearchCategory.PRODUCTS)) continue;
            collection.add(t);
        }
        gameViewModel.techNodesProducts = FlowKt.asStateFlow((MutableStateFlow)StateFlowKt.MutableStateFlow((Object)((List)collection)));
        iterable3 = TechCatalog.INSTANCE.getALL_TECHS();
        gameViewModel = this;
        boolean bl4 = false;
        $this$filterTo\2 = iterable2;
        Collection collection2 = new ArrayList();
        boolean bl5 = false;
        for (Object t : $this$filterTo\5) {
            TechTreeNode techTreeNode = (TechTreeNode)t;
            boolean bl6 = false;
            if (!(techTreeNode.getCategory() == ResearchCategory.INDUSTRY)) continue;
            collection2.add(t);
        }
        gameViewModel.techNodesIndustry = FlowKt.asStateFlow((MutableStateFlow)StateFlowKt.MutableStateFlow((Object)((List)collection2)));
        iterable2 = TechCatalog.INSTANCE.getALL_TECHS();
        gameViewModel = this;
        boolean bl7 = false;
        $this$filterTo\5 = iterable;
        Collection collection3 = new ArrayList();
        boolean bl8 = false;
        for (Object t : $this$filterTo\8) {
            TechTreeNode techTreeNode = (TechTreeNode)t;
            boolean bl9 = false;
            if (!(techTreeNode.getCategory() == ResearchCategory.COMPANY)) continue;
            collection3.add(t);
        }
        gameViewModel.techNodesCompany = FlowKt.asStateFlow((MutableStateFlow)StateFlowKt.MutableStateFlow((Object)((List)collection3)));
        iterable = TechCatalog.INSTANCE.getALL_TECHS();
        gameViewModel = this;
        boolean bl10 = false;
        $this$filterTo\8 = $this$filter\10;
        Collection collection4 = new ArrayList();
        boolean bl11 = false;
        for (Object t : $this$filterTo\11) {
            TechTreeNode techTreeNode = (TechTreeNode)t;
            boolean bl12 = false;
            if (!(techTreeNode.getCategory() == ResearchCategory.PERSONAL)) continue;
            collection4.add(t);
        }
        gameViewModel.techNodesPersonal = FlowKt.asStateFlow((MutableStateFlow)StateFlowKt.MutableStateFlow((Object)((List)collection4)));
    }

    @NotNull
    public final StateFlow<Boolean> getSoundEffectsEnabled() {
        return this.soundEffectsEnabled;
    }

    @NotNull
    public final StateFlow<Boolean> getNewsAlertsEnabled() {
        return this.newsAlertsEnabled;
    }

    public final void toggleSoundEffects(boolean enabled) {
        BuildersKt.launch$default((CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), null, null, (Function2)((Function2)new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, enabled, null){
            int label;
            final /* synthetic */ GameViewModel this$0;
            final /* synthetic */ boolean $enabled;
            {
                this.this$0 = $receiver;
                this.$enabled = $enabled;
                super(2, $completion);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public final Object invokeSuspend(Object $result) {
                Object object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure((Object)$result);
                        this.label = 1;
                        Object object2 = GameViewModel.access$getSettingsRepository$p(this.this$0).setSoundEnabled(this.$enabled, (Continuation<? super Unit>)((Continuation)this));
                        if (object2 != object) return Unit.INSTANCE;
                        return object;
                    }
                    case 1: {
                        ResultKt.throwOnFailure((Object)$result);
                        Object object2 = $result;
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            public final Continuation<Unit> create(Object value, Continuation<?> $completion) {
                return (Continuation)new /* invalid duplicate definition of identical inner class */;
            }

            public final Object invoke(CoroutineScope p1, Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        }), (int)3, null);
    }

    public final void toggleNewsAlerts(boolean enabled) {
        BuildersKt.launch$default((CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), null, null, (Function2)((Function2)new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, enabled, null){
            int label;
            final /* synthetic */ GameViewModel this$0;
            final /* synthetic */ boolean $enabled;
            {
                this.this$0 = $receiver;
                this.$enabled = $enabled;
                super(2, $completion);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public final Object invokeSuspend(Object $result) {
                Object object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure((Object)$result);
                        this.label = 1;
                        Object object2 = GameViewModel.access$getSettingsRepository$p(this.this$0).setNotificationsEnabled(this.$enabled, (Continuation<? super Unit>)((Continuation)this));
                        if (object2 != object) return Unit.INSTANCE;
                        return object;
                    }
                    case 1: {
                        ResultKt.throwOnFailure((Object)$result);
                        Object object2 = $result;
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            public final Continuation<Unit> create(Object value, Continuation<?> $completion) {
                return (Continuation)new /* invalid duplicate definition of identical inner class */;
            }

            public final Object invoke(CoroutineScope p1, Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        }), (int)3, null);
    }

    @NotNull
    public final StateFlow<GameState> getGameState() {
        return this.gameState;
    }

    @NotNull
    public final StateFlow<Boolean> getHasSavedGame() {
        return this.hasSavedGame;
    }

    @NotNull
    public final StateFlow<SaveSummary> getSaveSummary() {
        return this.saveSummary;
    }

    @NotNull
    public final StateFlow<Boolean> getShowDailyReportDialog() {
        return this.showDailyReportDialog;
    }

    @NotNull
    public final StateFlow<Boolean> getShowNewsChronicleDialog() {
        return this.showNewsChronicleDialog;
    }

    @NotNull
    public final StateFlow<Boolean> getShowEndgameDialog() {
        return this.showEndgameDialog;
    }

    @NotNull
    public final StateFlow<Boolean> getShowMilestoneScreen() {
        return this.showMilestoneScreen;
    }

    @NotNull
    public final StateFlow<Boolean> getShowCrossroadsDialog() {
        return this.showCrossroadsDialog;
    }

    @NotNull
    public final StateFlow<String> getSnackBarMessage() {
        return this.snackBarMessage;
    }

    @NotNull
    public final StateFlow<List<TechTreeNode>> getTechNodesProducts() {
        return this.techNodesProducts;
    }

    @NotNull
    public final StateFlow<List<TechTreeNode>> getTechNodesIndustry() {
        return this.techNodesIndustry;
    }

    @NotNull
    public final StateFlow<List<TechTreeNode>> getTechNodesCompany() {
        return this.techNodesCompany;
    }

    @NotNull
    public final StateFlow<List<TechTreeNode>> getTechNodesPersonal() {
        return this.techNodesPersonal;
    }

    public final void clearSnackBar() {
        this._snackBarMessage.setValue(null);
    }

    public final void dismissDailyReport() {
        this._showDailyReportDialog.setValue((Object)false);
    }

    public final void dismissNewsChronicle() {
        this._showNewsChronicleDialog.setValue((Object)false);
    }

    public final void openNewsChronicle() {
        this._showNewsChronicleDialog.setValue((Object)true);
    }

    public final void dismissEndgameDialog() {
        this._showEndgameDialog.setValue((Object)false);
    }

    public final void openEndgameDialog() {
        this._showEndgameDialog.setValue((Object)true);
    }

    public final void dismissCrossroadsDialog() {
        this._showCrossroadsDialog.setValue((Object)false);
    }

    public final void chooseCrossroadsOption(boolean incorporate) {
        this._showCrossroadsDialog.setValue((Object)false);
        if (incorporate) {
            GameState gameState;
            GameState gameState2;
            Object object;
            MutableStateFlow<GameState> mutableStateFlow = this._gameState;
            boolean bl = false;
            do {
                object = mutableStateFlow.getValue();
                gameState = (GameState)object;
                boolean bl2 = false;
            } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, NetWorthPhase.CORPORATE, false, null, null, null, null, null, null, false, null, null, null, -1, 262079, null))));
            this._snackBarMessage.setValue((Object)"Phase Shift: Welcome to Corporate Life.");
        } else {
            GameState gameState;
            GameState gameState3;
            Object object;
            MutableStateFlow<GameState> mutableStateFlow = this._gameState;
            boolean bl = false;
            do {
                object = mutableStateFlow.getValue();
                gameState = (GameState)object;
                boolean bl3 = false;
            } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState3 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, true, "You chose the peaceful farm life. Barnaby is proud.", 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -3145729, 262143, null))));
        }
    }

    /*
     * Unable to fully structure code
     */
    @NotNull
    public final GuidanceState evaluateAction(@NotNull GameAction action) {
        Intrinsics.checkNotNullParameter((Object)action, (String)"action");
        state = (GameState)this._gameState.getValue();
        if (state.getNetWorthPhase() == NetWorthPhase.CORPORATE) {
            return GuidanceState.HIDDEN;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[state.getNetWorthPhase().ordinal()]) {
            case 1: {
                var3_3 = action;
                if (!(var3_3 instanceof GameAction.Construct)) ** GOTO lbl23
                var5_5 = DefaultBuildings.INSTANCE.getInitialBuildings();
                for (T var7_9 : var5_5) {
                    it\2 = (Building)var7_9;
                    $i$a$-find-GameViewModel$evaluateAction$b$1\2\157\0 = false;
                    if (!Intrinsics.areEqual((Object)it\2.getId(), (Object)((GameAction.Construct)action).getBuildingId())) continue;
                    v0 = var7_9;
                    ** GOTO lbl17
                }
                v0 = null;
lbl17:
                // 2 sources

                b = v0;
                if (b != null && state.getCash() >= b.getBaseCost() * (double)2) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                v1 = GuidanceState.BAD;
                break;
lbl23:
                // 1 sources

                if (var3_3 instanceof GameAction.Upgrade) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                if (var3_3 instanceof GameAction.Research) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                if (var3_3 instanceof GameAction.Contract) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                if (var3_3 instanceof GameAction.Sell) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                throw new NoWhenBranchMatchedException();
            }
            case 2: {
                var3_4 = action;
                if (!(var3_4 instanceof GameAction.Construct)) ** GOTO lbl55
                var5_6 = DefaultBuildings.INSTANCE.getInitialBuildings();
                for (T var7_10 : var5_6) {
                    it\4 = (Building)var7_10;
                    $i$a$-find-GameViewModel$evaluateAction$b$2\4\169\0 = false;
                    if (!Intrinsics.areEqual((Object)it\4.getId(), (Object)((GameAction.Construct)action).getBuildingId())) continue;
                    v2 = var7_10;
                    ** GOTO lbl47
                }
                v2 = null;
lbl47:
                // 2 sources

                v3 = b = (Building)v2;
                if ((v3 != null ? v3.getType() : null) == BuildingType.RD_LAB) ** GOTO lbl51
                v4 = b;
                if ((v4 != null ? v4.getType() : null) != BuildingType.PROCESSING_PLANT) ** GOTO lbl53
lbl51:
                // 2 sources

                v1 = GuidanceState.BAD;
                break;
lbl53:
                // 1 sources

                v1 = GuidanceState.GOOD;
                break;
lbl55:
                // 1 sources

                if (var3_4 instanceof GameAction.Upgrade) {
                    v1 = GuidanceState.BAD;
                    break;
                }
                if (var3_4 instanceof GameAction.Research) {
                    v1 = GuidanceState.BAD;
                    break;
                }
                if (var3_4 instanceof GameAction.Contract) {
                    v1 = GuidanceState.BAD;
                    break;
                }
                if (var3_4 instanceof GameAction.Sell) {
                    v1 = GuidanceState.GOOD;
                    break;
                }
                throw new NoWhenBranchMatchedException();
            }
            case 3: {
                v1 = GuidanceState.HIDDEN;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return v1;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @NotNull
    public final GameState checkAchievements(@NotNull GameState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        currentAchievements = state.getAchievements();
        newlyUnlockedName = null;
        newlyUnlockedEmoji = null;
        newlyUnlockedEmoji = "\ud83c\udfc6";
        $this$map\1 = currentAchievements;
        $i$f$map\1\191 = false;
        var8_7 = $this$map\1;
        destination\2 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\1, (int)10));
        $i$f$mapTo\2\2833 = false;
        for (T item\2 : $this$mapTo\2) {
            block108: {
                block107: {
                    var13_12 = (Achievement)item\2;
                    var29_48 = destination\2;
                    $i$a$-map-GameViewModel$checkAchievements$updatedAchievements$1\3\2835\0 = false;
                    if (!ach\3.isUnlocked()) break block107;
                    v0 = ach\3;
                    break block108;
                }
                var15_14 = ach\3.getId();
                tmp = -1;
                switch (var15_14.hashCode()) {
                    case -1208749197: {
                        if (var15_14.equals("absurd_stakhanovite")) {
                            tmp = 1;
                        }
                        break;
                    }
                    case 1731603350: {
                        if (var15_14.equals("absurd_golden_pitchfork")) {
                            tmp = 2;
                        }
                        break;
                    }
                    case -1469309720: {
                        if (var15_14.equals("milestone_industrial_age")) {
                            tmp = 3;
                        }
                        break;
                    }
                    case -1855519229: {
                        if (var15_14.equals("milestone_artisan_mastery")) {
                            tmp = 4;
                        }
                        break;
                    }
                    case -1776475979: {
                        if (var15_14.equals("milestone_reputation_titan")) {
                            tmp = 5;
                        }
                        break;
                    }
                    case -139287493: {
                        if (var15_14.equals("tycoon_ten_grand")) {
                            tmp = 6;
                        }
                        break;
                    }
                    case -246664506: {
                        if (var15_14.equals("milestone_first_trade")) {
                            tmp = 7;
                        }
                        break;
                    }
                    case -261427002: {
                        if (var15_14.equals("milestone_first_drops")) {
                            tmp = 8;
                        }
                        break;
                    }
                    case 1451969605: {
                        if (var15_14.equals("absurd_sour_fortune")) {
                            tmp = 9;
                        }
                        break;
                    }
                    case 1668948949: {
                        if (var15_14.equals("milestone_cold_chain")) {
                            tmp = 10;
                        }
                        break;
                    }
                    case -2135817725: {
                        if (var15_14.equals("tycoon_mega_fortune")) {
                            tmp = 11;
                        }
                        break;
                    }
                    case -838285607: {
                        if (var15_14.equals("absurd_subprime_farmer")) {
                            tmp = 12;
                        }
                        break;
                    }
                    case 965791746: {
                        if (var15_14.equals("milestone_rd_debut")) {
                            tmp = 13;
                        }
                        break;
                    }
                    case 1653490140: {
                        if (var15_14.equals("tycoon_debt_free_baron")) {
                            tmp = 14;
                        }
                        break;
                    }
                    case 1333285450: {
                        if (var15_14.equals("tycoon_executive_mindset")) {
                            tmp = 15;
                        }
                        break;
                    }
                    case -1917524026: {
                        if (var15_14.equals("absurd_foreclosure_tightrope")) {
                            tmp = 16;
                        }
                        break;
                    }
                    case -572410316: {
                        if (var15_14.equals("tycoon_diamond_cowbell")) {
                            tmp = 17;
                        }
                        break;
                    }
                    case -316265040: {
                        if (var15_14.equals("milestone_century_club")) {
                            tmp = 18;
                        }
                        break;
                    }
                    case 1884690575: {
                        if (var15_14.equals("tycoon_hostile_takeover")) {
                            tmp = 19;
                        }
                        break;
                    }
                    case 461844475: {
                        if (var15_14.equals("absurd_cheese_hoarder")) {
                            tmp = 20;
                        }
                        break;
                    }
                    case 1253212271: {
                        if (var15_14.equals("tycoon_conglomerate")) {
                            tmp = 21;
                        }
                        break;
                    }
                }
                block23 : switch (tmp) {
                    case 8: {
                        $this$any\4 = state.getInventory();
                        $i$f$any\4\197 = false;
                        if ($this$any\4 instanceof Collection && ((Collection)$this$any\4).isEmpty()) {
                            v1 = false;
                            break;
                        }
                        for (T element\4 : $this$any\4) {
                            it\5 = (InventoryBatch)element\4;
                            $i$a$-any-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$1\5\2837\3 = false;
                            if (!Intrinsics.areEqual((Object)it\5.getItemId(), (Object)ProductCatalog.INSTANCE.getRAW_MILK().getId())) continue;
                            v1 = true;
                            break block23;
                        }
                        v1 = false;
                        break;
                    }
                    case 7: {
                        if (state.getTodaySoldUnits().isEmpty() == false || state.getTotalDaysPlayed() > 1) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 13: {
                        if (!((Collection)state.getUnlockedTechIds()).isEmpty()) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 3: {
                        $this$any\6 = state.getBuildings();
                        $i$f$any\6\200 = false;
                        if ($this$any\6 instanceof Collection && ((Collection)$this$any\6).isEmpty()) {
                            v1 = false;
                            break;
                        }
                        for (T element\6 : $this$any\6) {
                            it\7 = (Building)element\6;
                            $i$a$-any-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$2\7\2840\3 = false;
                            if (!(it\7.isConstructed() != false && it\7.getType() != BuildingType.PASTURE && it\7.getType() != BuildingType.RD_LAB)) continue;
                            v1 = true;
                            break block23;
                        }
                        v1 = false;
                        break;
                    }
                    case 10: {
                        if (state.getPlayerSkills().getLevel(SkillType.COLD_CHAIN_LOGISTICS) > 0) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 4: {
                        if (state.getUnlockedTechIds().contains("tech_cheese_aging")) ** GOTO lbl192
                        $this$any\8 = state.getInventory();
                        $i$f$any\8\202 = false;
                        if (!($this$any\8 instanceof Collection) || !((Collection)$this$any\8).isEmpty()) ** GOTO lbl183
                        v2 = false;
                        ** GOTO lbl191
lbl183:
                        // 2 sources

                        for (Iterator<T> element\8 : $this$any\8) {
                            it\9 = (InventoryBatch)element\8 /* !! */ ;
                            $i$a$-any-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$3\9\2843\3 = false;
                            var22_40 = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId()};
                            if (!CollectionsKt.listOf((Object[])var22_40).contains(it\9.getItemId())) continue;
                            v2 = true;
                            ** GOTO lbl191
                        }
                        v2 = false;
lbl191:
                        // 3 sources

                        if (!v2) ** GOTO lbl194
lbl192:
                        // 2 sources

                        v1 = true;
                        break;
lbl194:
                        // 1 sources

                        v1 = false;
                        break;
                    }
                    case 5: {
                        if (state.getReputation() >= 50) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 18: {
                        if (state.getDay() >= 25) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 6: {
                        if (state.getCash() >= 10000.0) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 14: {
                        if (state.getNetWorth() >= 20000.0 && state.getBank().getTotalDebt() <= 0.0) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 15: {
                        if (state.getPlayerSkills().getHustlerLevel() >= 3 || state.getPlayerSkills().getEfficiencyExpertLevel() >= 3 || state.getPlayerSkills().getSilverTongueLevel() >= 3) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 21: {
                        $this$count\10 = state.getBuildings();
                        $i$f$count\10\210 = false;
                        if ($this$count\10 instanceof Collection && ((Collection)$this$count\10).isEmpty()) {
                            v3 = 0;
                        } else {
                            count\10 = 0;
                            element\8 /* !! */  = $this$count\10.iterator();
                            while (element\8 /* !! */ .hasNext()) {
                                element\10 = element\8 /* !! */ .next();
                                it\11 = (Building)element\10;
                                $i$a$-count-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$4\11\2847\3 = false;
                                if (!it\11.isConstructed() || ++count\10 >= 0) continue;
                                CollectionsKt.throwCountOverflow();
                            }
                            v3 = count\10;
                        }
                        if (v3 >= 4) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 19: {
                        if (!((Collection)state.getSubsidiaryCompanyIds()).isEmpty()) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 17: {
                        if (state.getEndgameChoice() == EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 11: {
                        if (state.getNetWorth() >= 50000.0) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 9: {
                        $this$filter\12 = state.getInventory();
                        $i$f$filter\12\216 = 0;
                        count\10 = $this$filter\12;
                        destination\13 /* !! */  = new ArrayList<E>();
                        $i$f$filterTo\13\2849 = false;
                        it\11 = $this$filterTo\13.iterator();
                        while (it\11.hasNext()) {
                            element\13 = it\11.next();
                            it\14 = (InventoryBatch)element\13;
                            $i$a$-filter-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$5\14\2850\3 = false;
                            if (!(it\14.isSpoiled() != false || Intrinsics.areEqual((Object)it\14.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId()) != false)) continue;
                            destination\13 /* !! */ .add(element\13);
                        }
                        $this$filter\12 = (List)destination\13 /* !! */ ;
                        $i$f$filter\12\216 = 0;
                        $this$filterTo\13 = $this$filter\12.iterator();
                        while ($this$filterTo\13.hasNext()) {
                            destination\13 /* !! */  = $this$filterTo\13.next();
                            $i$f$filterTo\13\2849 = (InventoryBatch)destination\13 /* !! */ ;
                            var25_44 = $i$f$filter\12\216;
                            $i$a$-sumOfInt-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$6\15\216\3 = false;
                            var26_45 = it\15.getQuantity();
                            $i$f$filter\12\216 = var25_44 + var26_45;
                        }
                        if ($i$f$filter\12\216 >= 10) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 12: {
                        if (state.getBank().getTotalDebt() >= 4000.0) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 1: {
                        if (state.getManualLaborCount() >= 5) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 20: {
                        $this$filter\16 = state.getInventory();
                        $i$f$filter\16\219 = false;
                        $this$filterTo\13 = $this$filter\16;
                        destination\17 = new ArrayList<E>();
                        $i$f$filterTo\17\2852 = false;
                        for (T element\17 : $this$filterTo\17) {
                            it\18 = (InventoryBatch)element\17;
                            $i$a$-filter-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$7\18\2853\3 = false;
                            var27_46 = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId(), ProductCatalog.INSTANCE.getCREAM().getId()};
                            if (!CollectionsKt.listOf((Object[])var27_46).contains(it\18.getItemId())) continue;
                            destination\17.add(element\17);
                        }
                        var16_15 = (List)destination\17;
                        var17_16 = 0;
                        for (T var19_23 : var16_15) {
                            $i$f$filterTo\17\2852 = (InventoryBatch)var19_23;
                            var25_44 = var17_16;
                            $i$a$-sumOfInt-GameViewModel$checkAchievements$updatedAchievements$1$shouldUnlock$8\19\219\3 = false;
                            var26_45 = it\19.getQuantity();
                            var17_16 = var25_44 + var26_45;
                        }
                        if (var17_16 >= 40) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 16: {
                        if (state.getBank().getDaysInDebt() >= 5) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    case 2: {
                        if (state.getEndgameChoice() == EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE) {
                            v1 = true;
                            break;
                        }
                        v1 = false;
                        break;
                    }
                    default: {
                        v1 = shouldUnlock\3 = false;
                    }
                }
                if (shouldUnlock\3) {
                    if (newlyUnlockedName == null) {
                        newlyUnlockedName = ach\3.getTitle();
                        newlyUnlockedEmoji = ach\3.getTrophyEmoji();
                    }
                    v0 = Achievement.copy$default((Achievement)ach\3, null, null, null, true, false, null, null, state.getDay(), 119, null);
                } else {
                    v0 = ach\3;
                }
            }
            var29_48.add(v0);
        }
        updatedAchievements = (List)destination\2;
        if (newlyUnlockedName != null) {
            this._snackBarMessage.setValue((Object)("\ud83c\udfc6 Achievement Unlocked: " + newlyUnlockedEmoji + " " + newlyUnlockedName + "!"));
        }
        return GameState.copy$default(state, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, updatedAchievements, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -16777217, 262143, null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private final int calculateDailyUsage(GameState state, String itemId) {
        block7: {
            block6: {
                needed = 0;
                if (!Intrinsics.areEqual((Object)itemId, (Object)"cow_feed")) break block6;
                var4_4 = state.getBuildings();
                var13_6 = needed;
                $i$f$filter\1\257 = 0;
                var6_10 = $this$filter\1;
                destination\2 /* !! */  = new ArrayList<E>();
                $i$f$filterTo\2\2856 = false;
                var9_18 = $this$filterTo\2.iterator();
                while (var9_18.hasNext()) {
                    element\2 = var9_18.next();
                    it\3 = (Building)element\2;
                    $i$a$-filter-GameViewModel$calculateDailyUsage$1\3\2857\0 = false;
                    if (!(it\3.getType() == BuildingType.PASTURE && it\3.isConstructed() != false && it\3.isOperational() != false)) continue;
                    destination\2 /* !! */ .add(element\2);
                }
                var14_28 = (List)destination\2 /* !! */ ;
                $this$filter\1 = var14_28;
                $i$f$filter\1\257 = 0;
                for (Collection destination\2 : $this$filter\1) {
                    $i$f$filterTo\2\2856 = (Building)destination\2 /* !! */ ;
                    var14_29 = $i$f$filter\1\257;
                    $i$a$-sumOfInt-GameViewModel$calculateDailyUsage$2\4\257\0 = false;
                    var15_34 = it\4.getLevel();
                    $i$f$filter\1\257 = var14_29 + var15_34;
                }
                var14_30 = $i$f$filter\1\257;
                needed = var13_6 + var14_30;
                break block7;
            }
            if (!Intrinsics.areEqual((Object)itemId, (Object)"glass_bottles")) break block7;
            $this$filter\1 = state.getBuildings();
            var13_7 = needed;
            $i$f$filter\5\260 = false;
            $this$filterTo\2 = $this$filter\5;
            destination\6 = new ArrayList<E>();
            $i$f$filterTo\6\2859 = false;
            for (T element\6 : $this$filterTo\6) {
                it\7 = (Building)element\6;
                $i$a$-filter-GameViewModel$calculateDailyUsage$3\7\2860\0 = false;
                if (!it\7.isConstructed() || !it\7.isOperational()) ** GOTO lbl-1000
                v0 = it\7.getActiveRecipe();
                if (Intrinsics.areEqual((Object)(v0 != null ? v0.getOutputItemId() : null), (Object)"pasteurized_milk")) {
                    v1 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v1 = false;
                }
                if (!v1) continue;
                destination\6.add(element\6);
            }
            var14_31 = (List)destination\6;
            var4_5 = var14_31;
            var5_9 = 0;
            for (T var7_13 : var4_5) {
                $i$f$filterTo\6\2859 = (Building)var7_13;
                var14_32 = var5_9;
                $i$a$-sumOfInt-GameViewModel$calculateDailyUsage$4\8\260\0 = false;
                var15_35 = it\8.getCurrentProcessingCapacity();
                var5_9 = var14_32 + var15_35;
            }
            var14_33 = var5_9;
            needed = var13_7 + var14_33;
        }
        return needed;
    }

    public final void endDay() {
        if (((GameState)this._gameState.getValue()).isGameOver()) {
            return;
        }
        if (((GameState)this._gameState.getValue()).getNetWorth() >= 100000.0 && ((GameState)this._gameState.getValue()).getNetWorthPhase() != NetWorthPhase.CORPORATE) {
            this._showCrossroadsDialog.setValue((Object)true);
            return;
        }
        BuildersKt.launch$default((CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), null, null, (Function2)((Function2)new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, null){
            int label;
            final /* synthetic */ GameViewModel this$0;
            {
                this.this$0 = $receiver;
                super(2, $completion);
            }

            /*
             * Could not resolve type clashes
             * Unable to fully structure code
             */
            public final Object invokeSuspend(Object $result) {
                var238_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure((Object)$result);
                        triggeredNews = null;
                        var3_4 = GameViewModel.access$get_gameState$p(this.this$0);
                        var4_5 = this.this$0;
                        $i$f$update\1\279 = false;
                        do {
                            prevValue\1 = $this$update\1.getValue();
                            currentState\2 = (GameState)prevValue\1;
                            $i$a$-update-GameViewModel$endDay$1$1\2\2812\0 = false;
                            currentDay\2 = currentState\2.getDay();
                            notes\2 = new ArrayList<E>();
                            newlyCompletedTechIds\2 = new ArrayList<E>();
                            b2bUnlocked\2 = currentState\2.isB2BUnlocked();
                            if (b2bUnlocked\2) ** GOTO lbl31
                            var14_18 = var13_14 = (Iterable)currentState\2.getMentors();
                            for (T var16_24 : var14_18) {
                                it\3 = (Mentor)var16_24;
                                $i$a$-find-GameViewModel$endDay$1$1$barnabyAffinity$1\3\286\2 = false;
                                if (!Intrinsics.areEqual((Object)it\3.getId(), (Object)"mentor_barnaby")) continue;
                                v0 = var16_24;
                                ** GOTO lbl27
                            }
                            v0 = null;
lbl27:
                            // 2 sources

                            v1 = var19_29 = (Mentor)v0;
                            v2 = barnabyAffinity\2 = v1 != null ? (var13_15 = v1.getHiddenAffinity()) : 0;
                            if (currentDay\2 >= 25 || currentState\2.getReputation() >= 20 || barnabyAffinity\2 >= 10) {
                                b2bUnlocked\2 = true;
                            }
lbl31:
                            // 4 sources

                            currentLiquidCash\2 = 0.0;
                            currentLiquidCash\2 = currentState\2.getCash();
                            workingInventory\2 = CollectionsKt.toMutableList((Collection)currentState\2.getInventory());
                            $this$forEach\4 = currentState\2.getAutoBuySubscriptions();
                            $i$f$forEach\4\296 = false;
                            var15_20 = $this$forEach\4.entrySet().iterator();
                            while (var15_20.hasNext()) {
                                it\3 = element\4 = (Map.Entry)var15_20.next();
                                $i$a$-forEach-GameViewModel$endDay$1$1$1\5\2814\2 = false;
                                itemId\5 = (String)it\3.getKey();
                                isActive\5 = (Boolean)it\3.getValue();
                                if (!isActive\5 || (needed\5 = GameViewModel.access$calculateDailyUsage(var4_5, currentState\2, (String)itemId\5)) <= 0) continue;
                                v3 = currentState\2.getMarketPrices().get(itemId\5);
                                spotPrice\5 = v3 != null ? v3.getCurrentPrice() : ProductCatalog.INSTANCE.getById((String)itemId\5).getBasePrice();
                                affordableUnits\5 = (int)(currentLiquidCash\2 / spotPrice\5);
                                unitsToBuy\5 = Math.min(affordableUnits\5, needed\5);
                                if (unitsToBuy\5 > 0) {
                                    currentLiquidCash\2 -= (double)unitsToBuy\5 * spotPrice\5;
                                    workingInventory\2.add(new InventoryBatch(null, (String)itemId\5, ProductCatalog.INSTANCE.getById((String)itemId\5).getName(), unitsToBuy\5, 1.0, ProductCatalog.INSTANCE.getById((String)itemId\5).getShelfLifeDays(), currentDay\2, 0.0f, false, 385, null));
                                    if (unitsToBuy\5 < needed\5) {
                                        v4 = notes\2.add("\u26a0\ufe0f Low Cash: Auto-Buy partially fulfilled " + unitsToBuy\5 + "/" + needed\5 + " units of " + ProductCatalog.INSTANCE.getById((String)itemId\5).getName() + ".");
                                        continue;
                                    }
                                    v4 = notes\2.add("\ud83d\uded2 Auto-Buy procured " + unitsToBuy\5 + " units of " + ProductCatalog.INSTANCE.getById((String)itemId\5).getName() + ".");
                                    continue;
                                }
                                v4 = notes\2.add("\u26a0\ufe0f Insufficient funds to Auto-Buy " + needed\5 + " units of " + ProductCatalog.INSTANCE.getById((String)itemId\5).getName() + ".");
                            }
                            activeNews\2 = null;
                            activeNews\2 = currentState\2.getActiveNewsEvent();
                            isNewEventTriggered\2 = false;
                            if (activeNews\2 != null) {
                                remaining\2 = activeNews\2.getRemainingDays() - 1;
                                if (remaining\2 > 0) {
                                    activeNews\2 = NewsEvent.copy$default(activeNews\2, null, null, null, null, 0.0, 0, remaining\2, 0.0, 0, null, 959, null);
                                } else {
                                    notes\2.add("Market buzz from '" + activeNews\2.getTitle() + "' has concluded.");
                                    activeNews\2 = null;
                                }
                            }
                            if (activeNews\2 == null && Random.Default.nextDouble() < 0.35) {
                                activeNews\2 = freshEvent\2 = NewsCatalog.INSTANCE.getRandomEvent();
                                isNewEventTriggered\2 = true;
                                triggeredNews = freshEvent\2;
                                notes\2.add("\ud83d\udcf0 Morning Chronicle: " + freshEvent\2.getTitle());
                            }
                            $i$a$-forEach-GameViewModel$endDay$1$1$1\5\2814\2 = it\3 = (Iterable)currentState\2.getMentors();
                            itemId\5 = $i$a$-forEach-GameViewModel$endDay$1$1$1\5\2814\2.iterator();
                            while (itemId\5.hasNext()) {
                                isActive\5 = itemId\5.next();
                                it\6 = (Mentor)isActive\5;
                                $i$a$-find-GameViewModel$endDay$1$1$barnabyBuff$1\6\353\2 = false;
                                if (!Intrinsics.areEqual((Object)it\6.getId(), (Object)"mentor_barnaby")) continue;
                                v5 = isActive\5;
                                ** GOTO lbl86
                            }
                            v5 = null;
lbl86:
                            // 2 sources

                            v6 = element\4 = (Mentor)v5;
                            barnabyBuff\2 = v6 != null ? v6.isBuffUnlocked() : false;
                            itemId\5 = $i$a$-forEach-GameViewModel$endDay$1$1$1\5\2814\2 = (Iterable)currentState\2.getMentors();
                            isActive\5 = itemId\5.iterator();
                            while (isActive\5.hasNext()) {
                                it\6 = isActive\5.next();
                                it\7 = (Mentor)it\6;
                                $i$a$-find-GameViewModel$endDay$1$1$chadBuff$1\7\354\2 = false;
                                if (!Intrinsics.areEqual((Object)it\7.getId(), (Object)"mentor_chad")) continue;
                                v7 = it\6;
                                ** GOTO lbl98
                            }
                            v7 = null;
lbl98:
                            // 2 sources

                            v8 = it\3 = (Mentor)v7;
                            chadBuff\2 = v8 != null ? v8.isBuffUnlocked() : false;
                            isActive\5 = itemId\5 = (Iterable)currentState\2.getMentors();
                            it\6 = isActive\5.iterator();
                            while (it\6.hasNext()) {
                                it\7 = it\6.next();
                                it\8 = (Mentor)it\7;
                                $i$a$-find-GameViewModel$endDay$1$1$sterlingBuff$1\8\355\2 = false;
                                if (!Intrinsics.areEqual((Object)it\8.getId(), (Object)"mentor_sterling")) continue;
                                v9 = it\7;
                                ** GOTO lbl110
                            }
                            v9 = null;
lbl110:
                            // 2 sources

                            v10 = $i$a$-forEach-GameViewModel$endDay$1$1$1\5\2814\2 = (Mentor)v9;
                            sterlingBuff\2 = v10 != null ? v10.isBuffUnlocked() : false;
                            $this$map\9 = currentState\2.getMentors();
                            $i$f$map\9\357 = false;
                            it\6 = $this$map\9;
                            destination\10 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\9, (int)10));
                            $i$f$mapTo\10\2816 = false;
                            for (T item\10 : $this$mapTo\10) {
                                var30_59 = (Mentor)item\10;
                                var31_60 = destination\10;
                                $i$a$-map-GameViewModel$endDay$1$1$updatedMentors$1\11\2818\2 = false;
                                probability\11 = (double)mentor\11.getHiddenAffinity() * 0.003;
                                if (mentor\11.getHiddenAffinity() > 0 && Random.Default.nextDouble() < probability\11) {
                                    notes\2.add("\u2709\ufe0f New unprompted advice from " + mentor\11.getName() + " in your Mentor Network!");
                                    msg\11 = new ChatMessage(null, "Just checking in! Remember to keep your daily operations smooth.", false, currentDay\2, null, 17, null);
                                    v11 = Mentor.copy$default(mentor\11, null, null, null, null, 0, null, false, CollectionsKt.plus((Collection)mentor\11.getChatHistory(), (Object)msg\11), false, null, false, false, 3711, null);
                                } else {
                                    v11 = Mentor.copy$default(mentor\11, null, null, null, null, 0, null, false, null, false, null, false, false, 3839, null);
                                }
                                var36_70 = v11;
                                var31_60.add(var36_70);
                            }
                            updatedMentors\2 = (List)destination\10;
                            if (currentState\2.getNetWorthPhase() == NetWorthPhase.TENSION && Random.Default.nextDouble() < 0.15) {
                                $this$map\12 = updatedMentors\2;
                                $i$f$map\12\369 = false;
                                $this$mapTo\10 = $this$map\12;
                                destination\13 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\12, (int)10));
                                $i$f$mapTo\13\2820 = false;
                                for (T item\13 : $this$mapTo\13) {
                                    mentor\11 = (Mentor)item\13;
                                    var31_60 = destination\13;
                                    $i$a$-map-GameViewModel$endDay$1$1$2\14\2822\2 = false;
                                    if (Intrinsics.areEqual((Object)mentor\14.getId(), (Object)"mentor_sterling")) {
                                        notes\2.add("\u2709\ufe0f Private message from " + mentor\14.getName() + "!");
                                        msg\14 = new ChatMessage(null, "Barnaby's anti-profit warnings will stall your growth. Ignore him and expand your operations.", false, currentDay\2, null, 17, null);
                                        v12 = Mentor.copy$default((Mentor)mentor\14, null, null, null, null, 0, null, false, CollectionsKt.plus((Collection)mentor\14.getChatHistory(), (Object)msg\14), false, null, false, false, 3967, null);
                                    } else {
                                        v12 = mentor\14;
                                    }
                                    var36_70 = v12;
                                    var31_60.add(var36_70);
                                }
                                updatedMentors\2 = (List)destination\13;
                            }
                            $this$filter\15 = currentState\2.getBuildings();
                            $i$f$filter\15\379 = false;
                            $i$f$mapTo\13\2820 = $this$filter\15;
                            destination\16 = new ArrayList<E>();
                            $i$f$filterTo\16\2824 = false;
                            mentor\14 = $this$filterTo\16.iterator();
                            while (mentor\14.hasNext()) {
                                element\16 = mentor\14.next();
                                it\17 = (Building)element\16;
                                $i$a$-filter-GameViewModel$endDay$1$1$rawMaintenance$1\17\2825\2 = false;
                                if (!(it\17.isConstructed() != false && it\17.isOperational() != false)) continue;
                                destination\16.add(element\16);
                            }
                            var23_36 = (List)destination\16;
                            var24_40 = 0.0;
                            for (T $i$f$filterTo\16\2824 : var23_36) {
                                mentor\14 = (Building)$i$f$filterTo\16\2824;
                                var39_79 = var24_40;
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$rawMaintenance$2\18\380\2 = false;
                                var41_80 = it\18.getCurrentMaintenance();
                                var24_40 = var39_79 + var41_80;
                            }
                            rawMaintenance\2 = var24_40;
                            maintenanceCost\2 = rawMaintenance\2 * (1.0 - currentState\2.getPlayerSkills().getMaintenanceDiscountPercent());
                            $this$filter\19 = currentState\2.getBuildings();
                            $i$f$filter\19\382 = false;
                            $i$a$-sumOfDouble-GameViewModel$endDay$1$1$rawMaintenance$2\18\380\2 = $this$filter\19;
                            destination\20 = new ArrayList<E>();
                            $i$f$filterTo\20\2827 = false;
                            for (E element\20 : $this$filterTo\20) {
                                it\21 = (Building)element\20;
                                $i$a$-filter-GameViewModel$endDay$1$1$feedCost$1\21\2828\2 = false;
                                if (!(it\21.isConstructed() != false && it\21.getType() == BuildingType.PASTURE && it\21.isOperational() != false)) continue;
                                destination\20.add(element\20);
                            }
                            var27_43 = (List)destination\20;
                            var50_99 = 0.0;
                            for (T var38_77 : var27_43) {
                                msg\11 = (Building)var38_77;
                                var39_79 = var50_99;
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$feedCost$2\22\383\2 = false;
                                var41_80 = (double)it\22.getLevel() * 12.0;
                                var50_99 = var39_79 + var41_80;
                            }
                            feedCost\2 = var50_99;
                            if (chadBuff\2) {
                                maintenanceCost\2 *= 0.85;
                                feedCost\2 *= 0.85;
                            }
                            interestCharge\2 = currentState\2.getBank().getDailyInterestCharge();
                            if (isNewEventTriggered\2) {
                                v13 = var38_78 = activeNews\2;
                                v14 = v13 != null ? (var56_102 = v13.getCashBonus()) : 0.0;
                            } else {
                                v14 = 0.0;
                            }
                            eventCashBonus\2 = v14;
                            executiveSalary\2 = currentState\2.getTotalExecutiveSalary();
                            if (executiveSalary\2 > 0.0) {
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$feedCost$2\22\383\2 = StringCompanionObject.INSTANCE;
                                it\21 = "%.2f";
                                $i$a$-filter-GameViewModel$endDay$1$1$feedCost$1\21\2828\2 = new Object[]{Boxing.boxDouble((double)executiveSalary\2)};
                                v15 = String.format((String)it\21, Arrays.copyOf($i$a$-filter-GameViewModel$endDay$1$1$feedCost$1\21\2828\2, $i$a$-filter-GameViewModel$endDay$1$1$feedCost$1\21\2828\2.length));
                                Intrinsics.checkNotNullExpressionValue((Object)v15, (String)"format(...)");
                                notes\2.add("\ud83d\udc54 Executive Payroll: -$" + v15);
                            }
                            $this$any\23 = currentState\2.getActiveExecutives();
                            $i$f$any\23\397 = false;
                            if (!($this$any\23 instanceof Collection) || !((Collection)$this$any\23).isEmpty()) ** GOTO lbl228
                            v16 = false;
                            ** GOTO lbl235
lbl228:
                            // 2 sources

                            for (Object element\23 : $this$any\23) {
                                it\24 = (Executive)element\23;
                                $i$a$-any-GameViewModel$endDay$1$1$hasCFO$1\24\2831\2 = false;
                                if (!(it\24.getRole() == ExecutiveRole.CFO)) continue;
                                v16 = true;
                                ** GOTO lbl235
                            }
                            v16 = false;
lbl235:
                            // 3 sources

                            hasCFO\2 = v16;
                            $this$any\25 = currentState\2.getActiveExecutives();
                            $i$f$any\25\398 = false;
                            if (!($this$any\25 instanceof Collection) || !((Collection)$this$any\25).isEmpty()) ** GOTO lbl241
                            v17 = false;
                            ** GOTO lbl250
lbl241:
                            // 1 sources

                            element\23 = $this$any\25.iterator();
                            while (element\23.hasNext()) {
                                element\25 = element\23.next();
                                it\26 = (Executive)element\25;
                                $i$a$-any-GameViewModel$endDay$1$1$hasCOO$1\26\2834\2 = false;
                                if (!(it\26.getRole() == ExecutiveRole.COO)) continue;
                                v17 = true;
                                ** GOTO lbl250
                            }
                            v17 = false;
lbl250:
                            // 3 sources

                            hasCOO\2 = v17;
                            $this$any\27 = currentState\2.getActiveExecutives();
                            $i$f$any\27\399 = false;
                            if (!($this$any\27 instanceof Collection) || !((Collection)$this$any\27).isEmpty()) ** GOTO lbl256
                            v18 = false;
                            ** GOTO lbl263
lbl256:
                            // 2 sources

                            for (E element\27 : $this$any\27) {
                                it\28 = (Executive)element\27;
                                $i$a$-any-GameViewModel$endDay$1$1$hasCISO$1\28\2837\2 = false;
                                if (!(it\28.getRole() == ExecutiveRole.CISO)) continue;
                                v18 = true;
                                ** GOTO lbl263
                            }
                            v18 = false;
lbl263:
                            // 3 sources

                            hasCISO\2 = v18;
                            $this$any\29 = currentState\2.getActiveExecutives();
                            $i$f$any\29\400 = false;
                            if (!($this$any\29 instanceof Collection) || !((Collection)$this$any\29).isEmpty()) ** GOTO lbl269
                            v19 = false;
                            ** GOTO lbl276
lbl269:
                            // 2 sources

                            for (T element\29 : $this$any\29) {
                                it\30 = (Executive)element\29;
                                $i$a$-any-GameViewModel$endDay$1$1$hasCMO$1\30\2840\2 = false;
                                if (!(it\30.getRole() == ExecutiveRole.CMO)) continue;
                                v19 = true;
                                ** GOTO lbl276
                            }
                            v19 = false;
lbl276:
                            // 3 sources

                            hasCMO\2 = v19;
                            investmentRate\2 = hasCFO\2 != false ? 0.025 : 0.005;
                            investmentEarnings\2 = currentState\2.getBank().getInvestedFunds() * investmentRate\2;
                            newInvestedFunds\2 = currentState\2.getBank().getInvestedFunds() + investmentEarnings\2;
                            if (investmentEarnings\2 > 0.0) {
                                var75_126 = StringCompanionObject.INSTANCE;
                                var76_127 = "%.2f";
                                var77_128 = new Object[]{Boxing.boxDouble((double)investmentEarnings\2)};
                                v20 = String.format(var76_127, Arrays.copyOf(var77_128, var77_128.length));
                                Intrinsics.checkNotNullExpressionValue((Object)v20, (String)"format(...)");
                                notes\2.add("\ud83d\udcc8 Investment Returns: +$" + v20 + " at " + investmentRate\2 * (double)100 + "%");
                            }
                            currentCash\2 = 0.0;
                            currentCash\2 = currentLiquidCash\2 - (maintenanceCost\2 + feedCost\2 + interestCharge\2 + executiveSalary\2) + eventCashBonus\2;
                            if (Random.Default.nextDouble() < 0.05) {
                                if (hasCISO\2) {
                                    v21 = Boxing.boxBoolean((boolean)notes\2.add("\ud83d\udee1\ufe0f CISO Blocked an attempted Digital Fraud hack on corporate accounts!"));
                                } else {
                                    stolen\2 = RangesKt.coerceAtMost((double)RangesKt.coerceAtLeast((double)(currentCash\2 * 0.1), (double)100.0), (double)2500.0);
                                    if (currentCash\2 >= stolen\2) {
                                        currentCash\2 -= stolen\2;
                                        var80_130 = StringCompanionObject.INSTANCE;
                                        var81_132 = "%.2f";
                                        var82_133 = new Object[]{Boxing.boxDouble((double)stolen\2)};
                                        v22 = String.format(var81_132, Arrays.copyOf(var82_133, var82_133.length));
                                        Intrinsics.checkNotNullExpressionValue((Object)v22, (String)"format(...)");
                                        notes\2.add("\u26a0\ufe0f Corporate Sabotage! Rival hackers stole $" + v22 + " from liquid reserves.");
                                    }
                                    v21 = Unit.INSTANCE;
                                }
                            }
                            remainingActiveProjects\2 = new ArrayList<E>();
                            effectiveBuildings\2 = new Ref.ObjectRef();
                            effectiveBuildings\2.element = currentState\2.getBuildings();
                            effectiveUnlockedTechs\2 = null;
                            effectiveUnlockedTechs\2 = currentState\2.getUnlockedTechIds();
                            newlyBuiltFacilitiesCount\2 = 0;
                            tempRemaining\2 = new ArrayList<E>();
                            $this$forEach\31 = currentState\2.getActiveProjects();
                            $i$f$forEach\31\431 = false;
                            block33: for (T element\31 : $this$forEach\31) {
                                project\32 = (ActiveProject)element\31;
                                $i$a$-forEach-GameViewModel$endDay$1$1$3\32\2842\2 = false;
                                speedMultiplier\32 = project\32.isDedicated() != false ? 2 : 1;
                                daysLeft\32 = project\32.getDaysRemaining() - speedMultiplier\32;
                                if (daysLeft\32 <= 0) {
                                    switch (endDay.WhenMappings.$EnumSwitchMapping$0[project\32.getType().ordinal()]) {
                                        case 1: {
                                            var91_168 = (Iterable)effectiveBuildings\2.element;
                                            var92_174 = effectiveBuildings\2;
                                            $i$f$map\33\437 = false;
                                            var94_179 = $this$map\33;
                                            destination\34 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\33, (int)10));
                                            $i$f$mapTo\34\2843 = false;
                                            for (T item\34 : $this$mapTo\34) {
                                                var99_188 = (Building)item\34;
                                                var100_189 = destination\34;
                                                $i$a$-map-GameViewModel$endDay$1$1$3$1\35\2845\36 = false;
                                                var100_189.add(Intrinsics.areEqual((Object)b\35.getId(), (Object)project\32.getTargetId()) != false ? Building.copy$default(b\35, null, null, null, null, 1, true, 0.0, 0.0, 0, 0, 0, null, null, true, null, null, 0, 0, 0, 0.0, 0, 0, 0, 8380367, null) : b\35);
                                            }
                                            var92_174.element = (List)destination\34;
                                            ++newlyBuiltFacilitiesCount\2;
                                            v23 = notes\2.add("\ud83d\udd28 Construction Finished: " + project\32.getTargetName() + " is now fully operational!");
                                            continue block33;
                                        }
                                        case 2: {
                                            $this$map\33 = (Iterable)effectiveBuildings\2.element;
                                            var92_174 = effectiveBuildings\2;
                                            $i$f$map\36\446 = false;
                                            $this$mapTo\34 = $this$map\36;
                                            destination\37 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\36, (int)10));
                                            $i$f$mapTo\37\2847 = false;
                                            for (T item\37 : $this$mapTo\37) {
                                                b\35 = (Building)item\37 /* !! */ ;
                                                var100_189 = destination\37;
                                                $i$a$-map-GameViewModel$endDay$1$1$3$2\38\2849\36 = false;
                                                var100_189.add(Intrinsics.areEqual((Object)b\38.getId(), (Object)project\32.getTargetId()) != false ? Building.copy$default((Building)b\38, null, null, null, null, project\32.getTargetLevel(), false, 0.0, 0.0, 0, 0, 0, null, null, false, null, null, 0, 0, 0, 0.0, 0, 0, 0, 0x7FFFEF, null) : b\38);
                                            }
                                            var92_174.element = (List)destination\37;
                                            v23 = notes\2.add("\u2b50 Upgrade Finished: " + project\32.getTargetName() + " is ready!");
                                            continue block33;
                                        }
                                        case 3: {
                                            effectiveUnlockedTechs\2 = SetsKt.plus(effectiveUnlockedTechs\2, (Object)project\32.getTargetId());
                                            newlyCompletedTechIds\2.add(project\32.getTargetId());
                                            v23 = notes\2.add("\ud83d\udd2c Research Finished: " + project\32.getTargetName() + " R&D milestone reached!");
                                            continue block33;
                                        }
                                        default: {
                                            throw new NoWhenBranchMatchedException();
                                        }
                                    }
                                }
                                v23 = tempRemaining\2.add(ActiveProject.copy$default(project\32, null, null, null, null, null, 0, daysLeft\32, 0, 0.0, false, 959, null));
                            }
                            $this$count\39 = effectiveUnlockedTechs\2;
                            $i$f$count\39\466 = false;
                            if ($this$count\39 instanceof Collection && ((Collection)$this$count\39).isEmpty()) {
                                v24 = 0;
                            } else {
                                count\39 = 0;
                                for (E element\39 : $this$count\39) {
                                    it\40 = (String)element\39;
                                    $i$a$-count-GameViewModel$endDay$1$1$newOwnedLabs$1\40\2854\2 = false;
                                    if (!StringsKt.startsWith$default((String)it\40, (String)"tech_dedicated_lab_", (boolean)false, (int)2, null) || ++count\39 >= 0) continue;
                                    CollectionsKt.throwCountOverflow();
                                }
                                v24 = count\39;
                            }
                            newOwnedLabs\2 = v24;
                            $this$count\41 = effectiveUnlockedTechs\2;
                            $i$f$count\41\467 = false;
                            if ($this$count\41 instanceof Collection && ((Collection)$this$count\41).isEmpty()) {
                                v25 = 0;
                            } else {
                                count\41 = 0;
                                for (T element\41 : $this$count\41) {
                                    it\42 = (String)element\41;
                                    $i$a$-count-GameViewModel$endDay$1$1$newOwnedCrews$1\42\2858\2 = false;
                                    if (!StringsKt.startsWith$default((String)it\42, (String)"tech_construction_crew_", (boolean)false, (int)2, null) || ++count\41 >= 0) continue;
                                    CollectionsKt.throwCountOverflow();
                                }
                                v25 = count\41;
                            }
                            newOwnedCrews\2 = v25;
                            $this$count\43 = tempRemaining\2;
                            $i$f$count\43\469 = false;
                            if ($this$count\43 instanceof Collection && ((Collection)$this$count\43).isEmpty()) {
                                v26 = 0;
                            } else {
                                count\43 = 0;
                                for (T element\43 : $this$count\43) {
                                    it\44 = (ActiveProject)element\43;
                                    $i$a$-count-GameViewModel$endDay$1$1$currentLabsUsed$1\44\2862\2 = false;
                                    if (!(it\44.isDedicated() != false && it\44.getType() == ProjectType.TECH_RESEARCH) || ++count\43 >= 0) continue;
                                    CollectionsKt.throwCountOverflow();
                                }
                                v26 = count\43;
                            }
                            currentLabsUsed\2 = v26;
                            $this$count\45 = tempRemaining\2;
                            $i$f$count\45\470 = false;
                            if ($this$count\45 instanceof Collection && ((Collection)$this$count\45).isEmpty()) {
                                v27 = 0;
                            } else {
                                count\45 = 0;
                                for (T element\45 : $this$count\45) {
                                    it\46 = (ActiveProject)element\45;
                                    $i$a$-count-GameViewModel$endDay$1$1$currentCrewsUsed$1\46\2866\2 = false;
                                    if (!(it\46.isDedicated() != false && (it\46.getType() == ProjectType.FACILITY_CONSTRUCTION || it\46.getType() == ProjectType.FACILITY_UPGRADE)) || ++count\45 >= 0) continue;
                                    CollectionsKt.throwCountOverflow();
                                }
                                v27 = count\45;
                            }
                            currentCrewsUsed\2 = v27;
                            freeLabs\2 = RangesKt.coerceAtLeast((int)(newOwnedLabs\2 - currentLabsUsed\2), (int)0);
                            freeCrews\2 = RangesKt.coerceAtLeast((int)(newOwnedCrews\2 - currentCrewsUsed\2), (int)0);
                            element\43 = ((Collection)tempRemaining\2).size();
                            for (i\2 = 0; i\2 < element\43; ++i\2) {
                                p\2 = (ActiveProject)tempRemaining\2.get(i\2);
                                if (p\2.isDedicated()) continue;
                                if (p\2.getType() == ProjectType.TECH_RESEARCH && freeLabs\2 > 0) {
                                    tempRemaining\2.set(i\2, ActiveProject.copy$default(p\2, null, null, null, null, null, 0, 0, 0, 0.0, true, 511, null));
                                    --freeLabs\2;
                                    continue;
                                }
                                if (p\2.getType() != ProjectType.FACILITY_CONSTRUCTION && p\2.getType() != ProjectType.FACILITY_UPGRADE || freeCrews\2 <= 0) continue;
                                tempRemaining\2.set(i\2, ActiveProject.copy$default(p\2, null, null, null, null, null, 0, 0, 0, 0.0, true, 511, null));
                                --freeCrews\2;
                            }
                            remainingActiveProjects\2.addAll(tempRemaining\2);
                            rawProducedUnits\2 = 0;
                            processedUnitsProduced\2 = 0;
                            operatingCostFromProcessing\2 = 0.0;
                            researchPointsGained\2 = 0;
                            hasColdChain\2 = currentState\2.getPlayerSkills().getLevel(SkillType.COLD_CHAIN_LOGISTICS) > 0;
                            hasHerdGenetics\2 = currentState\2.getPlayerSkills().getLevel(SkillType.BOVINE_GENETICS) > 0;
                            hasAlpineSubsidiary\2 = currentState\2.getSubsidiaryCompanyIds().contains("rival_alpine_bovine");
                            $this$filter\47 = (Iterable)effectiveBuildings\2.element;
                            $i$f$filter\47\501 = false;
                            item\37 /* !! */  = $this$filter\47;
                            destination\48 = new ArrayList<E>();
                            $i$f$filterTo\48\2868 = false;
                            $i$a$-map-GameViewModel$endDay$1$1$3$2\38\2849\36 = $this$filterTo\48.iterator();
                            while ($i$a$-map-GameViewModel$endDay$1$1$3$2\38\2849\36.hasNext()) {
                                element\48 = $i$a$-map-GameViewModel$endDay$1$1$3$2\38\2849\36.next();
                                it\49 = (Building)element\48;
                                $i$a$-filter-GameViewModel$endDay$1$1$4\49\2869\2 = false;
                                if (!(it\49.isConstructed() != false && it\49.isOperational() != false && it\49.getSabotagedDaysRemaining() <= 0)) continue;
                                destination\48.add(element\48);
                            }
                            $this$filter\47 = (List)destination\48;
                            $i$f$forEach\50\501 = false;
                            $this$filterTo\48 = $this$forEach\50.iterator();
                            while ($this$filterTo\48.hasNext()) {
                                element\50 /* !! */  = $this$filterTo\48.next();
                                building\51 = (Building)element\50 /* !! */ ;
                                $i$a$-forEach-GameViewModel$endDay$1$1$5\51\2871\2 = false;
                                if (building\51.getType() == BuildingType.PASTURE) {
                                    extraYield\51 = hasHerdGenetics\2 != false ? 2 * building\51.getLevel() : 0;
                                    yield\51 = building\51.getCurrentDailyRawProduction() + extraYield\51;
                                    if (hasAlpineSubsidiary\2) {
                                        yield\51 = RangesKt.coerceAtLeast((int)((int)((double)yield\51 * 1.3 + 0.5)), (int)(yield\51 + 1));
                                    }
                                    if (barnabyBuff\2) {
                                        yield\51 = (int)((double)yield\51 * 1.1);
                                    }
                                    feedRequired\51 = building\51.getLevel();
                                    $this$filter\52 = workingInventory\2;
                                    $i$f$filter\52\511 = false;
                                    var107_233 = $this$filter\52;
                                    destination\53 = new ArrayList<E>();
                                    $i$f$filterTo\53\2872 = false;
                                    for (T element\53 : $this$filterTo\53) {
                                        it\54 = (InventoryBatch)element\53;
                                        $i$a$-filter-GameViewModel$endDay$1$1$5$availableFeedBatches$1\54\2873\55 = false;
                                        if (!Intrinsics.areEqual((Object)it\54.getItemId(), (Object)"cow_feed")) continue;
                                        destination\53.add(element\53);
                                    }
                                    availableFeedBatches\51 = (List)destination\53;
                                    $i$f$filter\52\511 = availableFeedBatches\51;
                                    $this$filterTo\53 = 0;
                                    for (E $i$f$filterTo\53\2872 : $i$f$filter\52\511) {
                                        var110_262 = (InventoryBatch)$i$f$filterTo\53\2872;
                                        var115_324 = $this$filterTo\53;
                                        $i$a$-sumOfInt-GameViewModel$endDay$1$1$5$totalFeedAvailable$1\55\512\55 = false;
                                        var116_331 = it\55.getQuantity();
                                        $this$filterTo\53 = var115_324 + var116_331;
                                    }
                                    totalFeedAvailable\51 = $this$filterTo\53;
                                    remainingToDeduct\51 = feedUsed\51 = Math.min(feedRequired\51, totalFeedAvailable\51);
                                    for (InventoryBatch batch\51 : availableFeedBatches\51) {
                                        if (remainingToDeduct\51 <= 0) break;
                                        bIndex\51 = workingInventory\2.indexOf(batch\51);
                                        if (bIndex\51 == -1) continue;
                                        take\51 = Math.min(batch\51.getQuantity(), remainingToDeduct\51);
                                        remainingToDeduct\51 -= take\51;
                                        if (batch\51.getQuantity() <= take\51) {
                                            v28 /* !! */  = workingInventory\2.remove(bIndex\51);
                                            continue;
                                        }
                                        workingInventory\2.set(bIndex\51, InventoryBatch.copy$default(batch\51, null, null, null, batch\51.getQuantity() - take\51, 0.0, 0, 0, 0.0f, false, 503, null));
                                        v28 /* !! */  = Unit.INSTANCE;
                                    }
                                    if (feedRequired\51 > 0) {
                                        yield\51 = (int)((double)yield\51 * ((double)feedUsed\51 / (double)feedRequired\51));
                                    }
                                    if (yield\51 <= 0) continue;
                                    rawProducedUnits\2 += yield\51;
                                    baseQuality\51 = (1.0 + (double)currentState\2.getReputation() / 100.0 * 0.7) * (hasHerdGenetics\2 != false ? 1.2 : 1.0);
                                    milkBatch\51 = new InventoryBatch(null, ProductCatalog.INSTANCE.getRAW_MILK().getId(), ProductCatalog.INSTANCE.getRAW_MILK().getName(), yield\51, (double)((int)(baseQuality\51 * 10.0)) / 10.0, ProductCatalog.INSTANCE.getRAW_MILK().getShelfLifeDays(), currentDay\2 + 1, 0.0f, false, 385, null);
                                    workingInventory\2.add(milkBatch\51);
                                    continue;
                                }
                                if (building\51.getType() != BuildingType.RD_LAB) continue;
                                researchPointsGained\2 += building\51.getCurrentResearchPoints();
                            }
                            hasWheySubsidiary\2 = currentState\2.getSubsidiaryCompanyIds().contains("rival_global_whey");
                            $this$filter\56 = (Iterable)effectiveBuildings\2.element;
                            $i$f$filter\56\554 = false;
                            element\50 /* !! */  = $this$filter\56;
                            destination\57 = new ArrayList<E>();
                            $i$f$filterTo\57\2876 = false;
                            for (T element\57 : $this$filterTo\57) {
                                it\58 = (Building)element\57;
                                $i$a$-filter-GameViewModel$endDay$1$1$6\58\2877\2 = false;
                                if (!(it\58.isConstructed() != false && it\58.isOperational() != false && it\58.getSabotagedDaysRemaining() <= 0 && it\58.getActiveRecipe() != null)) continue;
                                destination\57.add(element\57);
                            }
                            $this$filter\56 = (List)destination\57;
                            $i$f$forEach\59\554 = false;
                            block47: for (E element\59 : $this$forEach\59) {
                                building\60 = (Building)element\59;
                                $i$a$-forEach-GameViewModel$endDay$1$1$7\60\2879\2 = false;
                                if (building\60.getActiveRecipe() == null) continue;
                                remainingCapacity\60 = building\60.getCurrentProcessingCapacity();
                                if (hasWheySubsidiary\2) {
                                    remainingCapacity\60 = RangesKt.coerceAtLeast((int)((int)((double)remainingCapacity\60 * 1.5 + 0.5)), (int)(remainingCapacity\60 + 1));
                                }
                                while (remainingCapacity\60 > 0) {
                                    $this$filter\61 = workingInventory\2;
                                    $i$f$filter\61\562 = false;
                                    remainingToDeduct\51 = $this$filter\61;
                                    destination\62 = new ArrayList<E>();
                                    $i$f$filterTo\62\2880 = false;
                                    for (T element\62 : $this$filterTo\62) {
                                        it\63 = (InventoryBatch)element\62;
                                        $i$a$-filter-GameViewModel$endDay$1$1$7$eligibleBatches$1\63\2881\64 = false;
                                        if (!(Intrinsics.areEqual((Object)it\63.getItemId(), (Object)recipe\60.getInputItemId()) != false && it\63.isSpoiled() == false && it\63.getQuantity() > 0)) continue;
                                        destination\62.add(element\62);
                                    }
                                    eligibleBatches\60 = (List)destination\62;
                                    $i$f$filter\61\562 = eligibleBatches\60;
                                    $this$filterTo\62 = 0;
                                    for (E $i$f$filterTo\62\2880 : $i$f$filter\61\562) {
                                        milkBatch\51 = (InventoryBatch)$i$f$filterTo\62\2880;
                                        var115_324 = $this$filterTo\62;
                                        $i$a$-sumOfInt-GameViewModel$endDay$1$1$7$totalAvailableInput$1\64\566\64 = false;
                                        var116_331 = it\64.getQuantity();
                                        $this$filterTo\62 = var115_324 + var116_331;
                                    }
                                    totalAvailableInput\60 = $this$filterTo\62;
                                    requestedInputUnits\60 = (int)((double)totalAvailableInput\60 * ((double)building\60.getAllocationPercentage() / 100.0));
                                    possibleBatches\60 = requestedInputUnits\60 / recipe\60.getInputQuantity();
                                    if (possibleBatches\60 <= 0) continue block47;
                                    batchesToProcess\60 = 0;
                                    batchesToProcess\60 = RangesKt.coerceAtMost((int)remainingCapacity\60, (int)possibleBatches\60);
                                    if (batchesToProcess\60 <= 0) continue block47;
                                    if (Intrinsics.areEqual((Object)recipe\60.getOutputItemId(), (Object)"pasteurized_milk")) {
                                        $this$filter\65 = workingInventory\2;
                                        $i$f$filter\65\577 = false;
                                        it\63 = $this$filter\65;
                                        destination\66 = new ArrayList<E>();
                                        $i$f$filterTo\66\2883 = false;
                                        for (T element\66 : $this$filterTo\66) {
                                            it\67 = (InventoryBatch)element\66;
                                            $i$a$-filter-GameViewModel$endDay$1$1$7$availableBottles$1\67\2884\64 = false;
                                            if (!Intrinsics.areEqual((Object)it\67.getItemId(), (Object)"glass_bottles")) continue;
                                            destination\66.add(element\66);
                                        }
                                        availableBottles\60 = (List)destination\66;
                                        $i$f$filter\65\577 = availableBottles\60;
                                        $this$filterTo\66 = 0;
                                        for (E $i$f$filterTo\66\2883 : $i$f$filter\65\577) {
                                            var120_344 = (InventoryBatch)$i$f$filterTo\66\2883;
                                            var115_324 = $this$filterTo\66;
                                            $i$a$-sumOfInt-GameViewModel$endDay$1$1$7$totalBottles$1\68\578\64 = false;
                                            var116_331 = it\68.getQuantity();
                                            $this$filterTo\66 = var115_324 + var116_331;
                                        }
                                        totalBottles\60 = $this$filterTo\66;
                                        bottlesNeeded\60 = batchesToProcess\60 * recipe\60.getOutputQuantity();
                                        if (totalBottles\60 < bottlesNeeded\60) {
                                            batchesToProcess\60 = totalBottles\60 / recipe\60.getOutputQuantity();
                                        }
                                        remainingBottles\60 = bottlesToConsume\60 = batchesToProcess\60 * recipe\60.getOutputQuantity();
                                        orderedBottles\60 = currentState\2.getInventoryMethod() == InventoryMethod.LIFO ? CollectionsKt.reversed((Iterable)availableBottles\60) : availableBottles\60;
                                        it\68 = orderedBottles\60.iterator();
                                        while (it\68.hasNext()) {
                                            b\60 = (InventoryBatch)it\68.next();
                                            if (remainingBottles\60 <= 0) break;
                                            bIndex\60 = workingInventory\2.indexOf(b\60);
                                            if (bIndex\60 == -1) continue;
                                            take\60 = RangesKt.coerceAtMost((int)remainingBottles\60, (int)b\60.getQuantity());
                                            remainingBottles\60 -= take\60;
                                            if (b\60.getQuantity() <= take\60) {
                                                v29 /* !! */  = workingInventory\2.remove(bIndex\60);
                                                continue;
                                            }
                                            workingInventory\2.set(bIndex\60, InventoryBatch.copy$default(b\60, null, null, null, b\60.getQuantity() - take\60, 0.0, 0, 0, 0.0f, false, 503, null));
                                            v29 /* !! */  = Unit.INSTANCE;
                                        }
                                    }
                                    if (batchesToProcess\60 <= 0) continue block47;
                                    totalInputNeeded\60 = batchesToProcess\60 * recipe\60.getInputQuantity();
                                    totalOutputProduced\60 = batchesToProcess\60 * recipe\60.getOutputQuantity();
                                    if (hasCOO\2) {
                                        totalOutputProduced\60 = (int)((double)totalOutputProduced\60 * 1.5);
                                    }
                                    remainingInputToConsume\60 = totalInputNeeded\60;
                                    weightedQualitySum\60 = 0.0;
                                    orderedBatches\60 = currentState\2.getInventoryMethod() == InventoryMethod.LIFO ? CollectionsKt.reversed((Iterable)eligibleBatches\60) : eligibleBatches\60;
                                    it\68 = orderedBatches\60.iterator();
                                    while (it\68.hasNext()) {
                                        batch\60 = (InventoryBatch)it\68.next();
                                        if (remainingInputToConsume\60 <= 0) break;
                                        batchIndex\60 = workingInventory\2.indexOf(batch\60);
                                        if (batchIndex\60 == -1) continue;
                                        unitsToTake\60 = RangesKt.coerceAtMost((int)remainingInputToConsume\60, (int)batch\60.getQuantity());
                                        weightedQualitySum\60 += (double)unitsToTake\60 * batch\60.getQuality();
                                        remainingInputToConsume\60 -= unitsToTake\60;
                                        if (batch\60.getQuantity() <= unitsToTake\60) {
                                            v30 /* !! */  = workingInventory\2.remove(batchIndex\60);
                                            continue;
                                        }
                                        workingInventory\2.set(batchIndex\60, InventoryBatch.copy$default(batch\60, null, null, null, batch\60.getQuantity() - unitsToTake\60, 0.0, 0, 0, 0.0f, false, 503, null));
                                        v30 /* !! */  = Unit.INSTANCE;
                                    }
                                    avgInputQuality\60 = totalInputNeeded\60 > 0 ? weightedQualitySum\60 / (double)totalInputNeeded\60 : 1.0;
                                    outputProduct\60 = ProductCatalog.INSTANCE.getById(recipe\60.getOutputItemId());
                                    outputQuality\60 = (double)((int)(avgInputQuality\60 * recipe\60.getQualityMultiplier() * 10.0)) / 10.0;
                                    shelfLife\60 = outputProduct\60.getShelfLifeDays() + (hasColdChain\2 != false ? 2 : 0);
                                    workingInventory\2.add(new InventoryBatch(null, outputProduct\60.getId(), outputProduct\60.getName(), totalOutputProduced\60, outputQuality\60, shelfLife\60, currentDay\2 + 1, 0.0f, false, 385, null));
                                    $this$forEach\69 = recipe\60.getByProducts();
                                    $i$f$forEach\69\650 = false;
                                    var133_402 = $this$forEach\69.entrySet().iterator();
                                    while (var133_402.hasNext()) {
                                        var135_416 = element\69 = var133_402.next();
                                        $i$a$-forEach-GameViewModel$endDay$1$1$7$1\70\2886\64 = false;
                                        byProdId\70 = var135_416.getKey();
                                        qtyPerBatch\70 = ((Number)var135_416.getValue()).intValue();
                                        byProductQty\70 = batchesToProcess\60 * qtyPerBatch\70;
                                        if (byProductQty\70 <= 0) continue;
                                        byProd\70 = ProductCatalog.INSTANCE.getById(byProdId\70);
                                        workingInventory\2.add(new InventoryBatch(null, byProd\70.getId(), byProd\70.getName(), byProductQty\70, outputQuality\60, byProd\70.getShelfLifeDays(), currentDay\2 + 1, 0.0f, false, 385, null));
                                    }
                                    extraOpCost\60 = (double)totalInputNeeded\60 * recipe\60.getExtraOperatingCostPerUnit();
                                    operatingCostFromProcessing\2 += extraOpCost\60;
                                    currentCash\2 -= extraOpCost\60;
                                    processedUnitsProduced\2 += totalOutputProduced\60;
                                    remainingCapacity\60 -= batchesToProcess\60;
                                }
                            }
                            updatedActiveContracts\2 = new ArrayList<E>();
                            completedContractsDelta\2 = 0;
                            contractsRevenueToday\2 = 0.0;
                            $this$forEach\71 = currentState\2.getActiveContracts();
                            $i$f$forEach\71\681 = false;
                            for (T element\71 : $this$forEach\71) {
                                contract\72 = (ContractOffer)element\71;
                                $i$a$-forEach-GameViewModel$endDay$1$1$8\72\2889\2 = false;
                                targetProdId\72 = contract\72.getTargetProduct();
                                targetProduct\72 = ProductCatalog.INSTANCE.getById(targetProdId\72);
                                batchesToProcess\60 = currentState\2.getRivalCompanies();
                                for (T totalOutputProduced\60 : batchesToProcess\60) {
                                    it\73 = (RivalCompany)totalOutputProduced\60;
                                    $i$a$-find-GameViewModel$endDay$1$1$8$rival$1\73\684\76 = false;
                                    if (!Intrinsics.areEqual((Object)it\73.getId(), (Object)contract\72.getRivalId())) continue;
                                    v31 = totalOutputProduced\60;
                                    ** GOTO lbl697
                                }
                                v31 = null;
lbl697:
                                // 2 sources

                                if ((v32 = (RivalCompany)v31) == null) {
                                    v32 = RivalCatalog.INSTANCE.getRivalById(contract\72.getRivalId());
                                }
                                rival\72 = v32;
                                dailyQuota\72 = contract\72.getRequiredQuantity();
                                $this$filter\74 = workingInventory\2;
                                $i$f$filter\74\688 = false;
                                totalOutputProduced\60 = $this$filter\74;
                                destination\75 = new ArrayList<E>();
                                $i$f$filterTo\75\2890 = false;
                                for (T element\75 : $this$filterTo\75) {
                                    it\76 = (InventoryBatch)element\75;
                                    $i$a$-filter-GameViewModel$endDay$1$1$8$eligibleBatches$1\76\2891\76 = false;
                                    if (!(Intrinsics.areEqual((Object)it\76.getItemId(), (Object)targetProdId\72) != false && it\76.isSpoiled() == false && it\76.getQuantity() > 0)) continue;
                                    destination\75.add(element\75);
                                }
                                eligibleBatches\72 = (List)destination\75;
                                $i$f$filter\74\688 = eligibleBatches\72;
                                $this$filterTo\75 = 0;
                                for (E $i$f$filterTo\75\2890 : $i$f$filter\74\688) {
                                    var115_325 = (InventoryBatch)$i$f$filterTo\75\2890;
                                    outputProduct\60 = $this$filterTo\75;
                                    $i$a$-sumOfInt-GameViewModel$endDay$1$1$8$totalAvailable$1\77\691\76 = false;
                                    unitsToTake\60 = it\77.getQuantity();
                                    $this$filterTo\75 = outputProduct\60 + unitsToTake\60;
                                }
                                totalAvailable\72 = $this$filterTo\75;
                                if (totalAvailable\72 >= dailyQuota\72) {
                                    remainingToDeduct\72 = dailyQuota\72;
                                    orderedBatches\72 = currentState\2.getInventoryMethod() == InventoryMethod.LIFO ? CollectionsKt.reversed((Iterable)eligibleBatches\72) : eligibleBatches\72;
                                    for (InventoryBatch batch\72 : orderedBatches\72) {
                                        if (remainingToDeduct\72 <= 0) break;
                                        bIndex\72 = workingInventory\2.indexOf(batch\72);
                                        if (bIndex\72 == -1) continue;
                                        take\72 = RangesKt.coerceAtMost((int)remainingToDeduct\72, (int)batch\72.getQuantity());
                                        remainingToDeduct\72 -= take\72;
                                        if (batch\72.getQuantity() <= take\72) {
                                            v33 /* !! */  = workingInventory\2.remove(bIndex\72);
                                            continue;
                                        }
                                        workingInventory\2.set(bIndex\72, InventoryBatch.copy$default(batch\72, null, null, null, batch\72.getQuantity() - take\72, 0.0, 0, 0, 0.0f, false, 503, null));
                                        v33 /* !! */  = Unit.INSTANCE;
                                    }
                                    currentCash\2 += contract\72.getPayoutAmount();
                                    contractsRevenueToday\2 += contract\72.getPayoutAmount();
                                    nextDaysRemaining\72 = contract\72.getDaysRemaining() - 1;
                                    newFulfilledDays\72 = contract\72.getFulfilledDays() + 1;
                                    newTotalPaidOut\72 = contract\72.getTotalPaidOut() + contract\72.getPayoutAmount();
                                    v34 = targetProduct\72.getName();
                                    v35 = rival\72.getName();
                                    $i$a$-filter-GameViewModel$endDay$1$1$8$eligibleBatches$1\76\2891\76 = "%.2f";
                                    var143_450 = new Object[]{Boxing.boxDouble((double)contract\72.getPayoutAmount())};
                                    v36 = String.format($i$a$-filter-GameViewModel$endDay$1$1$8$eligibleBatches$1\76\2891\76, Arrays.copyOf(var143_450, var143_450.length));
                                    Intrinsics.checkNotNullExpressionValue((Object)v36, (String)"format(...)");
                                    notes\2.add("\ud83e\udd1d B2B Delivery: Supplied " + dailyQuota\72 + "x " + v34 + " to " + v35 + " (+$" + v36 + ").");
                                    if (nextDaysRemaining\72 <= 0) {
                                        ++completedContractsDelta\2;
                                        v37 = notes\2.add("\ud83c\udf89 Contract Complete: Fulfilled all terms with " + rival\72.getName() + "!");
                                        continue;
                                    }
                                    v37 = updatedActiveContracts\2.add(ContractOffer.copy$default(contract\72, null, null, null, 0, 0.0, 0, false, nextDaysRemaining\72, 0.0, 0, 0.0, newFulfilledDays\72, 0, newTotalPaidOut\72, 0, false, 55167, null));
                                    continue;
                                }
                                currentCash\2 -= contract\72.getPenaltyAmount();
                                nextDaysRemaining\72 = contract\72.getDaysRemaining() - 1;
                                newFailedDays\72 = contract\72.getFailedDays() + 1;
                                v38 = targetProduct\72.getName();
                                v39 = rival\72.getName();
                                newFulfilledDays\72 = "%.2f";
                                bIndex\72 = new Object[]{Boxing.boxDouble((double)contract\72.getPenaltyAmount())};
                                v40 = String.format(newFulfilledDays\72, Arrays.copyOf(bIndex\72, bIndex\72.length));
                                Intrinsics.checkNotNullExpressionValue((Object)v40, (String)"format(...)");
                                notes\2.add("\u26a0\ufe0f Contract Default: Shortfall on " + dailyQuota\72 + "x " + v38 + " for " + v39 + "! Fined -$" + v40 + ".");
                                if (nextDaysRemaining\72 <= 0) {
                                    ++completedContractsDelta\2;
                                    v37 = notes\2.add("\u274c Contract Expired: Defaulted on contract term with " + rival\72.getName() + ".");
                                    continue;
                                }
                                v37 = updatedActiveContracts\2.add(ContractOffer.copy$default(contract\72, null, null, null, 0, 0.0, 0, false, nextDaysRemaining\72, 0.0, 0, 0.0, 0, newFailedDays\72, 0.0, 0, false, 61311, null));
                            }
                            autoSellRevenueToday\2 = 0.0;
                            autoSoldUnits\2 = new LinkedHashMap<K, V>();
                            $this$forEach\78 = currentState\2.getAutoSellSubscriptions();
                            $i$f$forEach\78\758 = false;
                            contract\72 = $this$forEach\78.entrySet().iterator();
                            while (contract\72.hasNext()) {
                                targetProdId\72 = element\78 = contract\72.next();
                                $i$a$-forEach-GameViewModel$endDay$1$1$9\79\2894\2 = false;
                                itemId\79 = targetProdId\72.getKey();
                                isActive\79 = targetProdId\72.getValue();
                                if (!isActive\79) continue;
                                $this$filter\80 = workingInventory\2;
                                $i$f$filter\80\760 = false;
                                newFulfilledDays\72 = $this$filter\80;
                                destination\81 = new ArrayList<E>();
                                $i$f$filterTo\81\2895 = false;
                                for (T element\81 : $this$filterTo\81) {
                                    it\82 = (InventoryBatch)element\81;
                                    $i$a$-filter-GameViewModel$endDay$1$1$9$eligibleBatches$1\82\2896\83 = false;
                                    if (!(Intrinsics.areEqual((Object)it\82.getItemId(), (Object)itemId\79) != false && it\82.isSpoiled() == false && it\82.getQuantity() > 0)) continue;
                                    destination\81.add(element\81);
                                }
                                eligibleBatches\79 = (List)destination\81;
                                totalSold\79 = 0;
                                itemRevenue\79 = 0.0;
                                v41 = marketState\79 = currentState\2.getMarketPrices().get(itemId\79);
                                baseMarketPrice\79 = v41 != null ? v41.getCurrentPrice() : ProductCatalog.INSTANCE.getById(itemId\79).getBasePrice();
                                repFactor\79 = (double)currentState\2.getReputation() * 0.003 * currentState\2.getPlayerSkills().getSilverTongueRepBonusMultiplier();
                                repMultiplier\79 = 1.0 + repFactor\79;
                                mooCorpBonus\79 = currentState\2.getSubsidiaryCompanyIds().contains("rival_moocorp") != false ? 1.2 : 1.0;
                                $i$f$forEach\69\650 = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId(), ProductCatalog.INSTANCE.getCREAM().getId()};
                                isGourmet\79 = CollectionsKt.listOf((Object[])$i$f$forEach\69\650).contains(itemId\79);
                                lactoBonus\79 = isGourmet\79 != false && currentState\2.getSubsidiaryCompanyIds().contains("rival_lacto_dynasty") != false ? 1.35 : 1.0;
                                endgameMultiplier\79 = currentState\2.getEndgamePriceMultiplier();
                                orderedBatches\79 = currentState\2.getInventoryMethod() == InventoryMethod.LIFO ? CollectionsKt.reversed((Iterable)eligibleBatches\79) : eligibleBatches\79;
                                for (InventoryBatch batch\79 : orderedBatches\79) {
                                    bIndex\79 = workingInventory\2.indexOf(batch\79);
                                    if (bIndex\79 == -1) continue;
                                    unitPrice\79 = baseMarketPrice\79 * (0.8 + batch\79.getQuality() * 0.2) * repMultiplier\79 * mooCorpBonus\79 * lactoBonus\79 * endgameMultiplier\79;
                                    batchRev\79 = (double)batch\79.getQuantity() * unitPrice\79;
                                    totalSold\79 += batch\79.getQuantity();
                                    itemRevenue\79 += batchRev\79;
                                    workingInventory\2.remove(bIndex\79);
                                }
                                if (totalSold\79 <= 0) continue;
                                autoSellRevenueToday\2 += itemRevenue\79;
                                currentCash\2 += itemRevenue\79;
                                autoSoldUnits\2.put(itemId\79, Boxing.boxInt((int)totalSold\79));
                                productName\79 = ProductCatalog.INSTANCE.getById(itemId\79).getName();
                                bIndex\79 = "%.2f";
                                var157_471 /* !! */  = new Object[]{Boxing.boxDouble((double)itemRevenue\79)};
                                v42 = String.format(bIndex\79, Arrays.copyOf(var157_471 /* !! */ , var157_471 /* !! */ .length));
                                Intrinsics.checkNotNullExpressionValue((Object)v42, (String)"format(...)");
                                notes\2.add("\ud83d\uded2 Auto-Sell: Sold " + totalSold\79 + " units of " + productName\79 + " for +$" + v42 + ".");
                            }
                            spoiledCount\2 = 0;
                            finalInventory\2 = new ArrayList<E>();
                            remainingCapacity\2 = globalCapacity\2 = currentState\2.getGlobalColdStorageCapacity();
                            perishables\2 = new ArrayList<E>();
                            nonPerishables\2 = new ArrayList<E>();
                            $this$forEach\83 = workingInventory\2;
                            $i$f$forEach\83\811 = 0;
                            totalSold\79 = $this$forEach\83.iterator();
                            while (totalSold\79.hasNext()) {
                                element\83 = totalSold\79.next();
                                batch\84 = (InventoryBatch)element\83;
                                $i$a$-forEach-GameViewModel$endDay$1$1$10\84\2899\2 = false;
                                if (batch\84.getDayProduced() > currentDay\2) {
                                    v43 = finalInventory\2.add(batch\84);
                                    continue;
                                }
                                if (Intrinsics.areEqual((Object)batch\84.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId())) {
                                    nextDaysSpoiled\84 = batch\84.getDaysUntilSpoiled() - 1.0f;
                                    if (nextDaysSpoiled\84 > 0.0f) {
                                        v43 = finalInventory\2.add(InventoryBatch.copy$default(batch\84, null, null, null, 0, 0.0, 0, 0, nextDaysSpoiled\84, false, 383, null));
                                        continue;
                                    }
                                    v43 = notes\2.add("Disposed of " + batch\84.getQuantity() + " units of rotting spoiled milk.");
                                    continue;
                                }
                                v43 = batch\84.getMaxShelfLife() >= 999 ? nonPerishables\2.add(InventoryBatch.copy$default(batch\84, null, null, null, 0, 0.0, 0, 0, batch\84.getDaysUntilSpoiled() - 1.0f, false, 383, null)) : perishables\2.add(batch\84);
                            }
                            finalInventory\2.addAll(nonPerishables\2);
                            $this$forEach\83 = currentState\2.getColdStoragePriority();
                            $i$f$forEach\83\811 = endDay.WhenMappings.$EnumSwitchMapping$1[$this$forEach\83.ordinal()];
                            switch ($i$f$forEach\83\811) {
                                case 1: {
                                    $this$sortBy\85 = perishables\2;
                                    $i$f$sortBy\85\832 = false;
                                    if ($this$sortBy\85.size() <= 1) break;
                                    CollectionsKt.sortWith((List)$this$sortBy\85, (Comparator)new Comparator(){

                                        /*
                                         * WARNING - void declaration
                                         */
                                        public final int compare(T a, T b) {
                                            void it\2;
                                            InventoryBatch inventoryBatch = (InventoryBatch)a;
                                            boolean bl = false;
                                            Comparable comparable = Float.valueOf(inventoryBatch.getDaysUntilSpoiled());
                                            inventoryBatch = (InventoryBatch)b;
                                            Comparable comparable2 = comparable;
                                            boolean bl2 = false;
                                            return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)Float.valueOf(it\2.getDaysUntilSpoiled()));
                                        }
                                    });
                                    break;
                                }
                                case 2: {
                                    $this$sortByDescending\86 = perishables\2;
                                    $i$f$sortByDescending\86\835 = false;
                                    if ($this$sortByDescending\86.size() <= 1) break;
                                    CollectionsKt.sortWith((List)$this$sortByDescending\86, (Comparator)new Comparator(){

                                        /*
                                         * WARNING - void declaration
                                         */
                                        public final int compare(T a, T b) {
                                            void it\2;
                                            InventoryBatch inventoryBatch = (InventoryBatch)b;
                                            boolean bl = false;
                                            Comparable comparable = Double.valueOf(ProductCatalog.INSTANCE.getById(inventoryBatch.getItemId()).getBasePrice());
                                            inventoryBatch = (InventoryBatch)a;
                                            Comparable comparable2 = comparable;
                                            boolean bl2 = false;
                                            return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)Double.valueOf(ProductCatalog.INSTANCE.getById(it\2.getItemId()).getBasePrice()));
                                        }
                                    });
                                    break;
                                }
                                case 3: {
                                    break;
                                }
                                default: {
                                    throw new NoWhenBranchMatchedException();
                                }
                            }
                            if (currentState\2.getColdStoragePriority() == ColdStoragePriority.MANUAL) {
                                $this$mapValues\87 = allocations\2 = currentState\2.getManualColdStorageAllocations();
                                $i$f$mapValues\87\842 = false;
                                batch\84 = $this$mapValues\87;
                                destination\88 = new LinkedHashMap<K, V>(MapsKt.mapCapacity((int)$this$mapValues\87.size()));
                                $i$f$mapValuesTo\88\2905 = false;
                                $this$associateByTo\89 = $this$mapValuesTo\88.entrySet();
                                $i$f$associateByTo\89\2906 = false;
                                it\82 = $this$associateByTo\89.iterator();
                                while (it\82.hasNext()) {
                                    element\89 /* !! */  = it\82.next();
                                    eligibleBatches\79 = (Map.Entry)element\89 /* !! */ ;
                                    outputProduct\60 = destination\88;
                                    $i$a$-associateByTo-MapsKt__MapsKt$mapValuesTo$1\90\2908\92 = false;
                                    var158_477 = (Map.Entry)element\89 /* !! */ ;
                                    var36_70 = it\90.getKey();
                                    var31_60 = outputProduct\60;
                                    $i$a$-mapValues-GameViewModel$endDay$1$1$itemAllocationsRemaining$1\91\2908\2 = false;
                                    var41_81 = Boxing.boxInt((int)((Number)it\91.getValue()).intValue());
                                    var31_60.put(var36_70, var41_81);
                                }
                                itemAllocationsRemaining\2 = MapsKt.toMutableMap((Map)destination\88);
                                for (InventoryBatch batch\2 : perishables\2) {
                                    allowedCapacity\2 = ((Number)itemAllocationsRemaining\2.getOrDefault(batch\2.getItemId(), Boxing.boxInt((int)0))).intValue();
                                    if (allowedCapacity\2 > 0) {
                                        if (batch\2.getQuantity() <= allowedCapacity\2) {
                                            itemAllocationsRemaining\2.put(batch\2.getItemId(), Boxing.boxInt((int)(allowedCapacity\2 - batch\2.getQuantity())));
                                            v44 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, 0, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 0.5f, true, 127, null));
                                            continue;
                                        }
                                        protectedQty\2 = allowedCapacity\2;
                                        exposedQty\2 = batch\2.getQuantity() - protectedQty\2;
                                        itemAllocationsRemaining\2.put(batch\2.getItemId(), Boxing.boxInt((int)0));
                                        finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, protectedQty\2, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 0.5f, true, 119, null));
                                        $this$associateByTo\89 = UUID.randomUUID().toString();
                                        Intrinsics.checkNotNullExpressionValue((Object)$this$associateByTo\89, (String)"toString(...)");
                                        v44 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, (String)$this$associateByTo\89, null, null, exposedQty\2, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 1.0f, false, 118, null));
                                        continue;
                                    }
                                    v44 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, 0, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 1.0f, false, 127, null));
                                }
                            } else {
                                allocations\2 = perishables\2.iterator();
                                while (allocations\2.hasNext()) {
                                    batch\2 = (InventoryBatch)allocations\2.next();
                                    if (remainingCapacity\2 > 0) {
                                        if (batch\2.getQuantity() <= remainingCapacity\2) {
                                            remainingCapacity\2 -= batch\2.getQuantity();
                                            v45 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, 0, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 0.5f, true, 127, null));
                                            continue;
                                        }
                                        protectedQty\2 = remainingCapacity\2;
                                        exposedQty\2 = batch\2.getQuantity() - protectedQty\2;
                                        remainingCapacity\2 = 0;
                                        finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, protectedQty\2, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 0.5f, true, 119, null));
                                        allowedCapacity\2 = UUID.randomUUID().toString();
                                        Intrinsics.checkNotNullExpressionValue((Object)allowedCapacity\2, (String)"toString(...)");
                                        v45 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, allowedCapacity\2, null, null, exposedQty\2, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 1.0f, false, 118, null));
                                        continue;
                                    }
                                    v45 = finalInventory\2.add(InventoryBatch.copy$default(batch\2, null, null, null, 0, 0.0, 0, 0, batch\2.getDaysUntilSpoiled() - 1.0f, false, 127, null));
                                }
                            }
                            evaluatedInventory\2 = new ArrayList<E>();
                            for (InventoryBatch batch\2 : finalInventory\2) {
                                if (batch\2.isSpoiled() && !Intrinsics.areEqual((Object)batch\2.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId())) {
                                    spoiledCount\2 += batch\2.getQuantity();
                                    evaluatedInventory\2.add(new InventoryBatch(null, ProductCatalog.INSTANCE.getSPOILED_MILK().getId(), ProductCatalog.INSTANCE.getSPOILED_MILK().getName(), batch\2.getQuantity(), 0.2, ProductCatalog.INSTANCE.getSPOILED_MILK().getShelfLifeDays(), currentDay\2 + 1, ProductCatalog.INSTANCE.getSPOILED_MILK().getShelfLifeDays(), false, 1, null));
                                    v46 = notes\2.add("\u26a0\ufe0f Spoilage Alert: " + batch\2.getQuantity() + "x " + batch\2.getItemName() + " turned sour!");
                                    continue;
                                }
                                v46 = evaluatedInventory\2.add(batch\2);
                            }
                            finalInventory\2.clear();
                            finalInventory\2.addAll(evaluatedInventory\2);
                            daysInDebt\2 = currentState\2.getBank().getDaysInDebt();
                            bankDebt\2 = currentState\2.getBank().getTotalDebt();
                            foreclosureSalesCount\2 = 0;
                            automatedSalesRevenue\2 = 0.0;
                            if (currentCash\2 < 0.0) {
                                deficit\2 = -currentCash\2;
                                bankDebt\2 += deficit\2;
                                currentCash\2 = 0.0;
                            }
                            if (bankDebt\2 > 0.0) {
                                if (++daysInDebt\2 >= 7 && ((Collection)finalInventory\2).isEmpty() == false) {
                                    notes\2.add("\ud83d\udea8 BANK FORECLOSURE: The bank seized inventory to cover outstanding debt!");
                                    iterator\2 = finalInventory\2.iterator();
                                    while (iterator\2.hasNext() && bankDebt\2 > 0.0) {
                                        batch\2 = (InventoryBatch)iterator\2.next();
                                        v47 = it\90 = currentState\2.getMarketPrices().get(batch\2.getItemId());
                                        marketPrice\2 = v47 != null ? (var144_455 = v47.getCurrentPrice()) : 1.0;
                                        distressPrice\2 = marketPrice\2 * 0.75;
                                        batchTotalDistressVal\2 = (double)batch\2.getQuantity() * distressPrice\2;
                                        if (batchTotalDistressVal\2 <= bankDebt\2) {
                                            bankDebt\2 -= batchTotalDistressVal\2;
                                            automatedSalesRevenue\2 += batchTotalDistressVal\2;
                                            foreclosureSalesCount\2 += batch\2.getQuantity();
                                            iterator\2.remove();
                                            continue;
                                        }
                                        unitsToLiquidate\2 = RangesKt.coerceAtMost((int)((int)Math.ceil(bankDebt\2 / distressPrice\2)), (int)batch\2.getQuantity());
                                        recovered\2 = (double)unitsToLiquidate\2 * distressPrice\2;
                                        bankDebt\2 = RangesKt.coerceAtLeast((double)(bankDebt\2 - recovered\2), (double)0.0);
                                        automatedSalesRevenue\2 += recovered\2;
                                        foreclosureSalesCount\2 += unitsToLiquidate\2;
                                        remainingUnits\2 = batch\2.getQuantity() - unitsToLiquidate\2;
                                        if (remainingUnits\2 <= 0) {
                                            iterator\2.remove();
                                            continue;
                                        }
                                        idx\2 = finalInventory\2.indexOf(batch\2);
                                        if (idx\2 == -1) continue;
                                        finalInventory\2.set(idx\2, InventoryBatch.copy$default(batch\2, null, null, null, remainingUnits\2, 0.0, 0, 0, 0.0f, false, 503, null));
                                    }
                                }
                            } else {
                                daysInDebt\2 = 0;
                            }
                            $this$mapValues\92 = currentState\2.getMarketPrices();
                            $i$f$mapValues\92\959 = false;
                            element\89 /* !! */  = $this$mapValues\92;
                            destination\93 = new LinkedHashMap<K, V>(MapsKt.mapCapacity((int)$this$mapValues\92.size()));
                            $i$f$mapValuesTo\93\2911 = false;
                            $this$associateByTo\94 = $this$mapValuesTo\93.entrySet();
                            $i$f$associateByTo\94\2912 = false;
                            for (T element\94 : $this$associateByTo\94) {
                                var172_504 = (Map.Entry)element\94 /* !! */ ;
                                remainingUnits\2 = destination\93;
                                $i$a$-associateByTo-MapsKt__MapsKt$mapValuesTo$1\95\2914\97 = false;
                                shelfLife\60 = (Map.Entry)element\94 /* !! */ ;
                                var36_70 = it\95.getKey();
                                var31_60 = remainingUnits\2;
                                $i$a$-mapValues-GameViewModel$endDay$1$1$newMarketPrices$1\96\2914\2 = false;
                                productId\96 = (String)shelfLife\60.getKey();
                                marketState\96 = (MarketItemState)shelfLife\60.getValue();
                                product\96 = ProductCatalog.INSTANCE.getById(productId\96);
                                base\96 = product\96.getBasePrice();
                                v48 = currentState\2.getTodaySoldUnits().get(productId\96);
                                v49 = (Integer)autoSoldUnits\2.get(productId\96);
                                soldYesterday\96 = (v48 != null ? v48 : 0) + (v49 != null ? v49 : 0);
                                rng\96 = Random.Default.nextDouble(0.85, 1.25);
                                repBonus\96 = 1.0 + (double)currentState\2.getReputation() * 0.003 * currentState\2.getPlayerSkills().getSilverTongueRepBonusMultiplier();
                                supplyPenalty\96 = RangesKt.coerceIn((double)(1.0 - (double)soldYesterday\96 * 0.01), (double)0.6, (double)1.0);
                                eventMult\96 = activeNews\2 != null ? (activeNews\2.getTargetProductId() == null || Intrinsics.areEqual((Object)activeNews\2.getTargetProductId(), (Object)productId\96) ? activeNews\2.getMultiplier() : 1.0) : 1.0;
                                calculatedPrice\96 = base\96 * rng\96 * repBonus\96 * supplyPenalty\96 * eventMult\96;
                                calculatedPrice\96 = (double)((int)(calculatedPrice\96 * 100.0)) / 100.0;
                                calculatedPrice\96 = RangesKt.coerceAtLeast((double)calculatedPrice\96, (double)0.1);
                                oldPrice\96 = marketState\96.getCurrentPrice();
                                changePercent\96 = oldPrice\96 > 0.0 ? (calculatedPrice\96 - oldPrice\96) / oldPrice\96 * 100.0 : 0.0;
                                history\96 = CollectionsKt.takeLast((List)CollectionsKt.plus((Collection)marketState\96.getPriceHistory(), (Object)Boxing.boxDouble((double)calculatedPrice\96)), (int)10);
                                var41_82 = MarketItemState.copy$default(marketState\96, null, calculatedPrice\96, 0.0, soldYesterday\96, (double)((int)(changePercent\96 * 10.0)) / 10.0, history\96, 5, null);
                                var31_60.put(var36_70, var41_82);
                            }
                            newMarketPrices\2 = destination\93;
                            $this$map\97 = currentState\2.getRivalCompanies();
                            $i$f$map\97\991 = false;
                            destination\93 = $this$map\97;
                            destination\98 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\97, (int)10));
                            $i$f$mapTo\98\2917 = false;
                            for (T item\98 : $this$mapTo\98) {
                                element\94 /* !! */  = (RivalCompany)item\98;
                                var31_60 = destination\98;
                                $i$a$-map-GameViewModel$endDay$1$1$updatedRivals$1\99\2919\2 = false;
                                smearDaysLeft\99 = rival\99.getSmearDaysRemaining() > 0 ? rival\99.getSmearDaysRemaining() - 1 : 0;
                                isStillSmeared\99 = smearDaysLeft\99 > 0;
                                stockChangePct\99 = isStillSmeared\99 != false ? Random.Default.nextDouble(-9.0, -3.0) : Random.Default.nextDouble(-3.5, 4.5);
                                newStock\99 = (double)((int)(rival\99.getStockPrice() * (1.0 + stockChangePct\99 / 100.0) * 100.0)) / 100.0;
                                powerDelta\99 = (float)(stockChangePct\99 * 0.01);
                                newPower\99 = RangesKt.coerceIn((float)(rival\99.getMarketPower() + powerDelta\99), (float)0.5f, (float)4.0f);
                                newHostility\99 = rival\99.getHostilityToPlayer();
                                if (rival\99.getTargetSector() != null) {
                                    $this$filter\100 = ProductCatalog.INSTANCE.getALL_PRODUCTS();
                                    $i$f$filter\100\1008 = false;
                                    productName\79 = $this$filter\100;
                                    destination\101 = new ArrayList<E>();
                                    $i$f$filterTo\101\2920 = false;
                                    for (T element\101 : $this$filterTo\101) {
                                        it\102 = (Product)element\101;
                                        $i$a$-filter-GameViewModel$endDay$1$1$updatedRivals$1$matchingProducts$1\102\2921\103 = false;
                                        if (!(it\102.getCategory() == rival\99.getTargetSector())) continue;
                                        destination\101.add(element\101);
                                    }
                                    matchingProducts\99 = (List)destination\101;
                                    $i$f$filter\100\1008 = matchingProducts\99;
                                    $this$filterTo\101 = 0;
                                    for (E $i$f$filterTo\101\2920 : $i$f$filter\100\1008) {
                                        var188_519 = (Product)$i$f$filterTo\101\2920;
                                        var192_548 = $this$filterTo\101;
                                        $i$a$-sumOfInt-GameViewModel$endDay$1$1$updatedRivals$1$soldInSector$1\103\1009\103 = false;
                                        v50 = currentState\2.getTodaySoldUnits().get(it\103.getId());
                                        var193_553 = v50 != null ? v50 : 0;
                                        $this$filterTo\101 = var192_548 + var193_553;
                                    }
                                    soldInSector\99 = $this$filterTo\101;
                                    if (soldInSector\99 > 50) {
                                        newHostility\99 += 2;
                                    }
                                }
                                v51 = lockout\99 = rival\99.getB2bLockoutDaysRemaining() > 0 ? rival\99.getB2bLockoutDaysRemaining() - 1 : 0;
                                if (newHostility\99 >= 10 && currentState\2.getNetWorthPhase() == NetWorthPhase.CORPORATE && Random.Default.nextDouble() < 0.2) {
                                    aiOffense\99 = rival\99.getOffenseRating() + Random.Default.nextInt(1, 20);
                                    if (aiOffense\99 > (playerDef\99 = currentState\2.getPlayerDefenseRating() + Random.Default.nextInt(1, 20))) {
                                        notes\2.add("\ud83d\udea8 CYBER BREACH: " + rival\99.getName() + " successfully hacked our network!");
                                        $this$filter\104 = (Iterable)effectiveBuildings\2.element;
                                        $i$f$filter\104\1026 = false;
                                        it\103 = $this$filter\104;
                                        destination\105 = new ArrayList<E>();
                                        $i$f$filterTo\105\2923 = false;
                                        for (Object element\105 : $this$filterTo\105) {
                                            it\106 = (Building)element\105;
                                            $i$a$-filter-GameViewModel$endDay$1$1$updatedRivals$1$vulnerableBuildings$1\106\2924\103 = false;
                                            if (!(it\106.isConstructed() != false && it\106.isOperational() != false)) continue;
                                            destination\105.add(element\105);
                                        }
                                        vulnerableBuildings\99 = (List)destination\105;
                                        targetedBuilding\99 = (Building)CollectionsKt.randomOrNull((Collection)vulnerableBuildings\99, (Random)((Random)Random.Default));
                                        if (targetedBuilding\99 != null) {
                                            $i$f$filter\104\1026 = (Iterable)effectiveBuildings\2.element;
                                            var192_549 = effectiveBuildings\2;
                                            $i$f$map\107\1030 = false;
                                            destination\105 = $this$map\107;
                                            destination\108 /* !! */  = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\107, (int)10));
                                            $i$f$mapTo\108\2926 = false;
                                            element\105 = $this$mapTo\108.iterator();
                                            while (element\105.hasNext()) {
                                                item\108 = element\105.next();
                                                $i$a$-filter-GameViewModel$endDay$1$1$updatedRivals$1$vulnerableBuildings$1\106\2924\103 = (Building)item\108;
                                                var193_554 = destination\108 /* !! */ ;
                                                $i$a$-map-GameViewModel$endDay$1$1$updatedRivals$1$1\109\2928\103 = false;
                                                var193_554.add(Intrinsics.areEqual((Object)b\109.getId(), (Object)targetedBuilding\99.getId()) != false ? Building.copy$default((Building)b\109, null, null, null, null, 0, false, 0.0, 0.0, 0, 0, 0, null, null, false, null, null, 0, 0, 0, 0.0, 1, 0, 0, 0x6FFFFF, null) : b\109);
                                            }
                                            var192_549.element = (List)destination\108 /* !! */ ;
                                            $this$filter\110 = finalInventory\2;
                                            $i$f$filter\110\1036 = false;
                                            destination\108 /* !! */  = $this$filter\110;
                                            destination\111 = new ArrayList<E>();
                                            $i$f$filterTo\111\2930 = false;
                                            for (E element\111 : $this$filterTo\111) {
                                                it\112 = (InventoryBatch)element\111;
                                                $i$a$-filter-GameViewModel$endDay$1$1$updatedRivals$1$toDelete$1\112\2931\103 = false;
                                                rec\112 = targetedBuilding\99.getActiveRecipe();
                                                v52 = rec\112 != null ? Intrinsics.areEqual((Object)rec\112.getInputItemId(), (Object)it\112.getItemId()) || Intrinsics.areEqual((Object)rec\112.getOutputItemId(), (Object)it\112.getItemId()) : Intrinsics.areEqual((Object)it\112.getItemId(), (Object)ProductCatalog.INSTANCE.getRAW_MILK().getId());
                                                if (!v52) continue;
                                                destination\111.add(element\111);
                                            }
                                            toDelete\99 = (List)destination\111;
                                            finalInventory\2.removeAll(toDelete\99);
                                            notes\2.add("\ud83d\udca5 SABOTAGE: Inventory at " + targetedBuilding\99.getName() + " was destroyed, and operations halted for 1 day!");
                                        }
                                        newHostility\99 -= 10;
                                    } else {
                                        notes\2.add("\ud83d\udee1\ufe0f CYBER DEFENSE: Successfully blocked a digital attack from " + rival\99.getName() + ".");
                                        newHostility\99 -= 5;
                                    }
                                    newHostility\99 = RangesKt.coerceAtLeast((int)newHostility\99, (int)0);
                                }
                                var199_571 = RangesKt.coerceAtLeast((double)newStock\99, (double)10.0);
                                var201_572 = (double)((int)(stockChangePct\99 * 10.0)) / 10.0;
                                toDelete\99 = (float)((int)((double)newPower\99 * 10.0)) / 10.0f;
                                $this$filter\110 = newHostility\99;
                                var36_70 = RivalCompany.copy$default((RivalCompany)rival\99, null, null, null, null, null, toDelete\99, var199_571, var201_572, null, null, null, 0, 0.0, null, null, isStillSmeared\99, smearDaysLeft\99, 0.0, 0, 0, null, $this$filter\110, lockout\99, 1998623, null);
                                var31_60.add(var36_70);
                            }
                            updatedRivals\2 = (List)destination\98;
                            $this$mapTo\98 = currentState\2.getRivalSharesOwned().entrySet();
                            var144_455 = 0.0;
                            for (T item\98 : $this$mapTo\98) {
                                rival\99 = (Map.Entry)item\98;
                                var39_79 = var144_455;
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$totalDividendsToday$1\113\1069\2 = false;
                                rivalId\113 = (String)rival\99.getKey();
                                shares\113 = ((Number)rival\99.getValue()).intValue();
                                shelfLife\60 = updatedRivals\2;
                                for (T productId\96 : shelfLife\60) {
                                    it\114 = (RivalCompany)productId\96;
                                    $i$a$-find-GameViewModel$endDay$1$1$totalDividendsToday$1$rival$1\114\1070\117 = false;
                                    if (!Intrinsics.areEqual((Object)it\114.getId(), (Object)rivalId\113)) continue;
                                    v53 = productId\96;
                                    ** GOTO lbl1173
                                }
                                v53 = null;
lbl1173:
                                // 2 sources

                                if ((v54 = (RivalCompany)v53) == null) {
                                    v54 = RivalCatalog.INSTANCE.getRivalById(rivalId\113);
                                }
                                rival\113 = v54;
                                var41_83 = (double)shares\113 * rival\113.getDailyDividendPerShare();
                                var144_455 = var39_79 + var41_83;
                            }
                            totalDividendsToday\2 = var144_455;
                            if (sterlingBuff\2) {
                                totalDividendsToday\2 *= 1.15;
                            }
                            if (totalDividendsToday\2 > 0.0) {
                                currentCash\2 += totalDividendsToday\2;
                                $this$mapTo\98 = StringCompanionObject.INSTANCE;
                                destination\98 = "%.2f";
                                $i$f$mapTo\98\2917 = new Object[]{Boxing.boxDouble((double)totalDividendsToday\2)};
                                v55 = String.format((String)destination\98, Arrays.copyOf($i$f$mapTo\98\2917, $i$f$mapTo\98\2917.length));
                                Intrinsics.checkNotNullExpressionValue((Object)v55, (String)"format(...)");
                                notes\2.add("\ud83d\udcc8 Stock Dividends: Earned +$" + v55 + " from corporate shareholdings.");
                            }
                            destination\98 = currentState\2.getPendingContractOffers();
                            $i$f$map\115\1083 = false;
                            $i$f$associateByTo\94\2912 = $this$map\115;
                            destination\116 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\115, (int)10));
                            $i$f$mapTo\116\2934 = false;
                            for (T item\116 : $this$mapTo\116) {
                                shares\113 = (ContractOffer)item\116;
                                var31_60 = destination\116;
                                $i$a$-map-GameViewModel$endDay$1$1$agedPendingOffers$1\117\2936\2 = false;
                                var36_70 = ContractOffer.copy$default((ContractOffer)it\117, null, null, null, 0, 0.0, 0, false, 0, 0.0, 0, 0.0, 0, 0, 0.0, it\117.getExpiresInDays() - 1, false, 49151, null);
                                var31_60.add(var36_70);
                            }
                            $this$map\115 = (List)destination\116;
                            $i$f$filter\118\1084 = false;
                            $this$mapTo\116 = $this$filter\118;
                            destination\119 = new ArrayList<E>();
                            $i$f$filterTo\119\2938 = false;
                            for (T element\119 : $this$filterTo\119) {
                                it\120 = (ContractOffer)element\119;
                                $i$a$-filter-GameViewModel$endDay$1$1$agedPendingOffers$2\120\2939\2 = false;
                                if (!(it\120.getExpiresInDays() > 0)) continue;
                                destination\119.add(element\119);
                            }
                            agedPendingOffers\2 = CollectionsKt.toMutableList((Collection)((List)destination\119));
                            $this$filter\121 = updatedRivals\2;
                            $i$f$filter\121\1088 = false;
                            destination\119 = $this$filter\121;
                            destination\122 = new ArrayList<E>();
                            $i$f$filterTo\122\2941 = 0;
                            for (T element\122 : $this$filterTo\122) {
                                it\123 = (RivalCompany)element\122;
                                $i$a$-filter-GameViewModel$endDay$1$1$nonSubsidiaryRivals$1\123\2942\2 = false;
                                if (!(currentState\2.getSubsidiaryCompanyIds().contains(it\123.getId()) == false)) continue;
                                destination\122.add(element\122);
                            }
                            nonSubsidiaryRivals\2 = (List)destination\122;
                            if (b2bUnlocked\2 && ((Collection)nonSubsidiaryRivals\2).isEmpty() == false && agedPendingOffers\2.size() < 4 && Random.Default.nextDouble() < 0.7) {
                                randomRival\2 = (RivalCompany)CollectionsKt.random((Collection)nonSubsidiaryRivals\2, (Random)((Random)Random.Default));
                                $this$filterTo\122 = new Product[]{ProductCatalog.INSTANCE.getRAW_MILK(), ProductCatalog.INSTANCE.getPASTEURIZED_MILK(), ProductCatalog.INSTANCE.getCREAM(), ProductCatalog.INSTANCE.getBUTTER(), ProductCatalog.INSTANCE.getAGED_CHEDDAR()};
                                candidateProducts\2 = CollectionsKt.listOf((Object[])$this$filterTo\122);
                                chosenProduct\2 = (Product)CollectionsKt.random((Collection)candidateProducts\2, (Random)((Random)Random.Default));
                                $i$f$filterTo\122\2941 = chosenProduct\2.getTier();
                                switch ($i$f$filterTo\122\2941) {
                                    case 1: {
                                        v56 = Random.Default.nextInt(10, 25);
                                        break;
                                    }
                                    case 2: {
                                        v56 = Random.Default.nextInt(5, 14);
                                        break;
                                    }
                                    default: {
                                        v56 = Random.Default.nextInt(3, 8);
                                    }
                                }
                                baseQuota\2 = v56;
                                v57 = element\122 = (MarketItemState)newMarketPrices\2.get(chosenProduct\2.getId());
                                spotPrice\2 = v57 != null ? (var146_456 = v57.getCurrentPrice()) : chosenProduct\2.getBasePrice();
                                cmoBonus\2 = hasCMO\2 != false ? 1.2 : 1.0;
                                sterlingMultiplier\2 = sterlingBuff\2 != false ? 1.15 : 1.0;
                                isJunk\2 = currentState\2.getReputation() < 10;
                                premiumMultiplier\2 = 0.0;
                                duration\2 = 0;
                                penaltyMultiplier\2 = 0.0;
                                if (isJunk\2) {
                                    premiumMultiplier\2 = Random.Default.nextDouble(0.7, 0.9);
                                    duration\2 = 1;
                                    penaltyMultiplier\2 = 1.5;
                                } else {
                                    repScale\2 = (double)currentState\2.getReputation() / 100.0;
                                    premiumMultiplier\2 = Random.Default.nextDouble(1.1, 1.3) + repScale\2 * 0.5;
                                    duration\2 = Random.Default.nextInt(2, 4) + currentState\2.getReputation() / 30;
                                    penaltyMultiplier\2 = 0.65;
                                    baseQuota\2 = (int)((double)baseQuota\2 * (1.0 + repScale\2 * 1.5));
                                }
                                dailyPayout\2 = (double)((int)(spotPrice\2 * (double)baseQuota\2 * premiumMultiplier\2 * cmoBonus\2 * sterlingMultiplier\2 * 100.0)) / 100.0;
                                penalty\2 = (double)((int)((dailyPayout\2 * penaltyMultiplier\2 + 50.0) * 100.0)) / 100.0;
                                newOffer\2 = new ContractOffer(null, randomRival\2.getId(), chosenProduct\2.getId(), baseQuota\2, dailyPayout\2, duration\2, false, 0, penalty\2, 0, 0.0, 0, 0, 0.0, Random.Default.nextInt(2, 4), isJunk\2 != false, 16065, null);
                                agedPendingOffers\2.add(newOffer\2);
                                junkTag\2 = isJunk\2 != false ? " [HIGH RISK]" : "";
                                notes\2.add("\ud83d\udcbc Inbound RFP: " + randomRival\2.getName() + " submitted a bulk supply contract for " + baseQuota\2 + "x " + chosenProduct\2.getName() + "/day." + junkTag\2);
                            }
                            v58 = isEndgameReady\2 = currentState\2.getSubsidiaryCompanyIds().size() >= RivalCatalog.INSTANCE.getALL_RIVALS().size() && currentState\2.isEndgameCompleted() == false && currentState\2.isEndgameTriggered() == false;
                            if (isEndgameReady\2) {
                                GameViewModel.access$get_showEndgameDialog$p(var4_5).setValue((Object)Boxing.boxBoolean((boolean)true));
                            }
                            baseQuota\2 = finalInventory\2;
                            var39_79 = currentCash\2;
                            var203_573 = 0.0;
                            for (T it\123 : baseQuota\2) {
                                $i$a$-filter-GameViewModel$endDay$1$1$nonSubsidiaryRivals$1\123\2942\2 = (InventoryBatch)it\123;
                                var213_578 = var203_573;
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$estimatedTotalNetWorth$1\124\1158\2 = false;
                                v59 = (MarketItemState)newMarketPrices\2.get(batch\124.getItemId());
                                price\124 = v59 != null ? v59.getCurrentPrice() : 1.0;
                                var217_580 = (double)batch\124.getQuantity() * price\124;
                                var203_573 = var213_578 + var217_580;
                            }
                            var213_578 = var203_573;
                            baseQuota\2 = currentState\2.getBuildings();
                            var39_79 += var213_578;
                            $i$f$filter\125\1161 = false;
                            element\119 = $this$filter\125;
                            destination\126 = new ArrayList<E>();
                            $i$f$filterTo\126\2944 = false;
                            for (T element\126 : $this$filterTo\126) {
                                it\127 = (Building)element\126;
                                $i$a$-filter-GameViewModel$endDay$1$1$estimatedTotalNetWorth$2\127\2945\2 = false;
                                if (!it\127.isConstructed()) continue;
                                destination\126.add(element\126);
                            }
                            var41_84 = (List)destination\126;
                            $this$filter\125 = var41_84;
                            var203_573 = 0.0;
                            for (E $i$f$filterTo\126\2944 : $this$filter\125) {
                                batch\124 = (Building)$i$f$filterTo\126\2944;
                                var213_578 = var203_573;
                                $i$a$-sumOfDouble-GameViewModel$endDay$1$1$estimatedTotalNetWorth$3\128\1161\2 = false;
                                var217_580 = it\128.getBaseCost() * (double)it\128.getLevel() * 0.75;
                                var203_573 = var213_578 + var217_580;
                            }
                            var213_578 = var203_573;
                            estimatedTotalNetWorth\2 = var39_79 + var213_578 + currentState\2.getTotalPortfolioValue() - bankDebt\2;
                            isBankrupt\2 = daysInDebt\2 >= 14 && estimatedTotalNetWorth\2 < 0.0;
                            nextDay\2 = currentDay\2 + 1;
                            report\2 = new DailyReport(currentDay\2, rawProducedUnits\2, processedUnitsProduced\2, spoiledCount\2, maintenanceCost\2 + operatingCostFromProcessing\2, feedCost\2, interestCharge\2, researchPointsGained\2, automatedSalesRevenue\2 + autoSellRevenueToday\2, foreclosureSalesCount\2, totalDividendsToday\2, activeNews\2, notes\2);
                            if (isNewEventTriggered\2) {
                                v60 = $i$f$filterTo\126\2944 = activeNews\2;
                                v61 = v60 != null ? (it\128 = v60.getReputationDelta()) : 0;
                            } else {
                                v61 = 0;
                            }
                            repGainedFromNews\2 = v61;
                            updatedReputation\2 = RangesKt.coerceIn((int)(currentState\2.getReputation() + repGainedFromNews\2), (int)0, (int)100);
                            $i$a$-sumOfDouble-GameViewModel$endDay$1$1$estimatedTotalNetWorth$3\128\1161\2 = finalInventory\2;
                            var31_61 = nextDay\2;
                            it\127 = 0;
                            for (T rival\113 : $i$a$-sumOfDouble-GameViewModel$endDay$1$1$estimatedTotalNetWorth$3\128\1161\2) {
                                duration\2 = (InventoryBatch)rival\113;
                                var36_71 = it\127;
                                $i$a$-sumOfInt-GameViewModel$endDay$1$1$newLogEntry$1\129\1186\2 = false;
                                var41_85 = it\129.getQuantity();
                                it\127 = var36_71 + var41_85;
                            }
                            var36_72 = it\127;
                            $i$a$-sumOfDouble-GameViewModel$endDay$1$1$estimatedTotalNetWorth$3\128\1161\2 = StringCompanionObject.INSTANCE;
                            it\127 = "%.2f";
                            $i$a$-filter-GameViewModel$endDay$1$1$estimatedTotalNetWorth$2\127\2945\2 = new Object[]{Boxing.boxDouble((double)currentCash\2)};
                            v62 = String.format(it\127, Arrays.copyOf($i$a$-filter-GameViewModel$endDay$1$1$estimatedTotalNetWorth$2\127\2945\2, $i$a$-filter-GameViewModel$endDay$1$1$estimatedTotalNetWorth$2\127\2945\2.length));
                            Intrinsics.checkNotNullExpressionValue((Object)v62, (String)"format(...)");
                            newLogEntry\2 = "Day " + var31_61 + " begun. Total Inventory: " + var36_72 + " units. Cash: $" + v62;
                            newLogs\2 = CollectionsKt.take((Iterable)CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)newLogEntry\2), (Iterable)currentState\2.getDailyLogs()), (int)20);
                            newPhase\2 = currentState\2.getNetWorthPhase() == NetWorthPhase.STARTUP && estimatedTotalNetWorth\2 >= 50000.0 ? NetWorthPhase.TENSION : currentState\2.getNetWorthPhase();
                            finalMentors\2 = updatedMentors\2;
                            isNewBoardroom\2 = currentState\2.getUnlockedFeatures().isBoardroomNew();
                            isNewStockMarket\2 = currentState\2.getUnlockedFeatures().isStockMarketNew();
                            v63 = shouldUnlockBoardroom\2 = currentState\2.getUnlockedFeatures().isBoardroomUnlocked() != false || estimatedTotalNetWorth\2 >= 50000.0;
                            if (!currentState\2.getUnlockedFeatures().isBoardroomUnlocked() && shouldUnlockBoardroom\2) {
                                isNewBoardroom\2 = true;
                                $this$map\130 = finalMentors\2;
                                $i$f$map\130\1199 = false;
                                targetedBuilding\99 = $this$map\130;
                                destination\131 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\130, (int)10));
                                $i$f$mapTo\131\2947 = false;
                                for (T item\131 : $this$mapTo\131) {
                                    destination\111 = (Mentor)item\131;
                                    var31_62 = destination\131;
                                    $i$a$-map-GameViewModel$endDay$1$1$13\132\2949\2 = false;
                                    if (Intrinsics.areEqual((Object)mentor\132.getId(), (Object)"mentor_sterling")) {
                                        notes\2.add("\u2709\ufe0f Private message from " + mentor\132.getName() + "!");
                                        msg1\132 = new ChatMessage(null, "Your net worth is swelling. It's time to build a Boardroom and hire real Executives.", false, currentDay\2, null, 17, null);
                                        msg2\132 = new ChatMessage(null, "Executives charge a daily salary, but provide powerful, permanent passive buffs.", false, currentDay\2, null, 17, null);
                                        msg3\132 = new ChatMessage(null, "For instance, a COO will boost your processing speed, while a CFO improves bank interest rates. Choose wisely based on your cash flow.", false, currentDay\2, null, 17, null);
                                        newUnlocks\132 = mentor\132.getUnlockedFeatures().contains("Explain the Boardroom") == false ? CollectionsKt.plus((Collection)mentor\132.getUnlockedFeatures(), (Object)"Explain the Boardroom") : mentor\132.getUnlockedFeatures();
                                        element\111 = CollectionsKt.plus((Collection)CollectionsKt.plus((Collection)CollectionsKt.plus((Collection)mentor\132.getChatHistory(), (Object)msg1\132), (Object)msg2\132), (Object)msg3\132);
                                        v64 = Mentor.copy$default((Mentor)mentor\132, null, null, null, null, 0, newUnlocks\132, false, element\111, false, null, false, false, 3679, null);
                                    } else {
                                        v64 = mentor\132;
                                    }
                                    var36_73 = v64;
                                    var31_62.add(var36_73);
                                }
                                finalMentors\2 = (List)destination\131;
                            }
                            v65 = shouldUnlockStockMarket\2 = currentState\2.getUnlockedFeatures().isStockMarketUnlocked() != false || newPhase\2 == NetWorthPhase.CORPORATE;
                            if (!currentState\2.getUnlockedFeatures().isStockMarketUnlocked() && shouldUnlockStockMarket\2) {
                                isNewStockMarket\2 = true;
                                $this$map\133 = finalMentors\2;
                                $i$f$map\133\1218 = false;
                                destination\131 = $this$map\133;
                                destination\134 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\133, (int)10));
                                $i$f$mapTo\134\2951 = false;
                                for (T item\134 : $this$mapTo\134) {
                                    $i$a$-map-GameViewModel$endDay$1$1$13\132\2949\2 = (Mentor)item\134;
                                    var31_63 = destination\134;
                                    $i$a$-map-GameViewModel$endDay$1$1$14\135\2953\2 = false;
                                    if (Intrinsics.areEqual((Object)mentor\135.getId(), (Object)"mentor_sterling")) {
                                        notes\2.add("\u2709\ufe0f Private message from " + mentor\135.getName() + "!");
                                        msg1\135 = new ChatMessage(null, "Welcome to the Corporate phase. The Stock Market is now open.", false, currentDay\2, null, 17, null);
                                        msg2\135 = new ChatMessage(null, "You can now purchase shares of your rival companies. Accumulate 51% to trigger a hostile takeover.", false, currentDay\2, null, 17, null);
                                        msg3\135 = new ChatMessage(null, "Subjugated subsidiaries will no longer compete against you and will provide massive empire-wide buffs. It's time to crush the competition.", false, currentDay\2, null, 17, null);
                                        newUnlocks\135 = mentor\135.getUnlockedFeatures().contains("Explain the Stock Market") == false ? CollectionsKt.plus((Collection)mentor\135.getUnlockedFeatures(), (Object)"Explain the Stock Market") : mentor\135.getUnlockedFeatures();
                                        var185_515 = CollectionsKt.plus((Collection)CollectionsKt.plus((Collection)CollectionsKt.plus((Collection)mentor\135.getChatHistory(), (Object)msg1\135), (Object)msg2\135), (Object)msg3\135);
                                        v66 = Mentor.copy$default((Mentor)mentor\135, null, null, null, null, 0, newUnlocks\135, false, var185_515, false, null, false, false, 3679, null);
                                    } else {
                                        v66 = mentor\135;
                                    }
                                    var36_74 = v66;
                                    var31_63.add(var36_74);
                                }
                                finalMentors\2 = (List)destination\134;
                            }
                            var177_510 = currentCash\2;
                            destination\134 = currentState\2.getResearchPoints() + researchPointsGained\2;
                            var189_526 = effectiveUnlockedTechs\2;
                            var190_534 = (List)effectiveBuildings\2.element;
                            var191_541 = currentState\2.getMaxDailyActions() + (barnabyBuff\2 != false ? 2 : 0);
                            var192_547 = currentState\2.getStats();
                            var193_557 = currentState\2.getStats().getTotalDaysPlayed() + 1;
                            var219_581 = currentState\2.getStats().getTotalMilkProduced() + (long)rawProducedUnits\2;
                            var221_582 = currentState\2.getStats().getTotalProductsProcessed() + (long)processedUnitsProduced\2;
                            var223_583 = currentState\2.getStats().getTotalSpoiledUnits() + (long)spoiledCount\2;
                            var225_584 = currentState\2.getStats().getTotalResearchPointsEarned() + researchPointsGained\2;
                            var226_585 = currentState\2.getStats().getTotalDividendsEarned() + totalDividendsToday\2;
                            var228_586 = currentState\2.getStats().getTotalCashEarned() + automatedSalesRevenue\2 + contractsRevenueToday\2 + totalDividendsToday\2;
                            var230_587 = currentState\2.getStats().getTotalContractsFulfilled() + completedContractsDelta\2;
                            var231_588 = currentState\2.getStats().getFacilitiesBuilt() + newlyBuiltFacilitiesCount\2;
                            var157_471 /* !! */  = LifetimeStats.copy$default((LifetimeStats)var192_547, var193_557, var228_586, var219_581, var221_582, var230_587, var231_588, var225_584, var226_585, var223_583, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0.0, 0, 67108352, null);
                            var192_547 = activeNews\2;
                            var193_552 = BankState.copy$default(currentState\2.getBank(), (double)((int)(bankDebt\2 * 100.0)) / 100.0, daysInDebt\2, 0.0, 0.0, 0, (double)((int)(newInvestedFunds\2 * 100.0)) / 100.0, 28, null);
                            var194_558 = MapsKt.emptyMap();
                            var195_563 = isBankrupt\2 != false ? "The Agricultural Credit Union foreclosed on all land and assets. Debt exceeded total enterprise value." : null;
                            var196_564 = currentState\2.getTotalDaysPlayed() + 1;
                            var185_514 = b2bUnlocked\2;
                            var197_569 = currentState\2.getCompletedContractsCount() + completedContractsDelta\2;
                            var232_589 = currentState\2.getTotalContractRevenueEarned() + contractsRevenueToday\2;
                            var234_590 = currentState\2.isEndgameTriggered() != false || isEndgameReady\2 != false;
                            var235_591 = finalMentors\2;
                            var236_592 = UnlockedFeatures.copy$default(currentState\2.getUnlockedFeatures(), shouldUnlockBoardroom\2 != false, shouldUnlockStockMarket\2 != false, false, isNewBoardroom\2 != false, isNewStockMarket\2 != false, false, 36, null);
                            newState\2 = GameState.copy$default(currentState\2, null, nextDay\2, var177_510, updatedReputation\2, destination\134, var189_526, null, var191_541, 0, 0, remainingActiveProjects\2, (LifetimeStats)var157_471 /* !! */ , (NewsEvent)var192_547, finalInventory\2, var190_534, newMarketPrices\2, var193_552, var194_558, newLogs\2, report\2, isBankrupt\2 != false, var195_563, var196_564, 0, null, updatedRivals\2, agedPendingOffers\2, updatedActiveContracts\2, var197_569, var232_589, null, null, var234_590 != false, false, null, 0.0, null, var235_591, newPhase\2, var185_514 != false, var236_592, null, null, null, null, null, false, null, null, null, -1048575167, 261662, null);
                            var4_5.checkAchievements(newState\2);
                            finalState\2 = newState\2;
                            for (String id\2 : newlyCompletedTechIds\2) {
                                finalState\2 = GameViewModel.access$applyResearchEffect(var4_5, finalState\2, id\2);
                            }
                        } while (!$this$update\1.compareAndSet(prevValue\1, (Object)(nextValue\1 = finalState\2)));
                        finalState = (GameState)GameViewModel.access$get_gameState$p(this.this$0).getValue();
                        if (finalState.getNetWorth() >= 100000.0 && !finalState.getHasFired100kEvent()) {
                            GameViewModel.access$get_gameState$p(this.this$0).setValue((Object)GameState.copy$default(finalState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, true, null, null, null, -1, 245759, null));
                            this.this$0.saveGame();
                            GameViewModel.access$get_showMilestoneScreen$p(this.this$0).setValue((Object)Boxing.boxBoolean((boolean)true));
                            return Unit.INSTANCE;
                        }
                        this.this$0.checkWinState();
                        this.this$0.saveGame();
                        if (triggeredNews != null) {
                            GameViewModel.access$get_showNewsChronicleDialog$p(this.this$0).setValue((Object)Boxing.boxBoolean((boolean)true));
                        } else {
                            GameViewModel.access$get_showDailyReportDialog$p(this.this$0).setValue((Object)Boxing.boxBoolean((boolean)true));
                        }
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            public final Continuation<Unit> create(Object value, Continuation<?> $completion) {
                return (Continuation)new /* invalid duplicate definition of identical inner class */;
            }

            public final Object invoke(CoroutineScope p1, Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        }), (int)3, null);
    }

    public final void depositFunds(double amount) {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            if (gameState3.getCash() < amount) {
                gameState = gameState3;
                continue;
            }
            gameState = GameState.copy$default(gameState3, null, 0, gameState3.getCash() - amount, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, BankState.copy$default(gameState3.getBank(), 0.0, 0, 0.0, 0.0, 0, gameState3.getBank().getInvestedFunds() + amount, 31, null), null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -65541, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void withdrawFunds(double amount) {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            double d = RangesKt.coerceAtMost((double)amount, (double)gameState3.getBank().getInvestedFunds());
            if (d <= 0.0) {
                gameState = gameState3;
                continue;
            }
            gameState = GameState.copy$default(gameState3, null, 0, gameState3.getCash() + d, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, BankState.copy$default(gameState3.getBank(), 0.0, 0, 0.0, 0.0, 0, gameState3.getBank().getInvestedFunds() - d, 31, null), null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -65541, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void processPlayerMessage(@NotNull String mentorId, @NotNull String messageText) {
        Intrinsics.checkNotNullParameter((Object)mentorId, (String)"mentorId");
        Intrinsics.checkNotNullParameter((Object)messageText, (String)"messageText");
        BuildersKt.launch$default((CoroutineScope)ViewModelKt.getViewModelScope((ViewModel)((ViewModel)this)), null, null, (Function2)((Function2)new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, messageText, mentorId, null){
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
            Object L$6;
            Object L$7;
            Object L$8;
            Object L$9;
            int I$0;
            int I$1;
            int I$2;
            long J$0;
            long J$1;
            long J$2;
            long J$3;
            int label;
            final /* synthetic */ GameViewModel this$0;
            final /* synthetic */ String $messageText;
            final /* synthetic */ String $mentorId;
            {
                this.this$0 = $receiver;
                this.$messageText = $messageText;
                this.$mentorId = $mentorId;
                super(2, $completion);
            }

            /*
             * Unable to fully structure code
             * Could not resolve type clashes
             */
            public final Object invokeSuspend(Object $result) {
                var99_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure((Object)$result);
                        currentState = (GameState)GameViewModel.access$get_gameState$p(this.this$0).getValue();
                        currentDay = currentState.getDay();
                        var6_5 = currentState.getMentors();
                        var7_6 = this.$mentorId;
                        var8_7 = var6_5;
                        var9_8 = var8_7.iterator();
                        while (var9_8.hasNext()) {
                            var10_11 = var9_8.next();
                            it\2 = (Mentor)var10_11;
                            $i$a$-find-GameViewModel$processPlayerMessage$1$mentorCheck$1\2\1339\0 = false;
                            if (!Intrinsics.areEqual((Object)it\2.getId(), (Object)var7_6)) continue;
                            v0 = var10_11;
                            ** GOTO lbl19
                        }
                        v0 = null;
lbl19:
                        // 2 sources

                        v1 = var5_16 = (Mentor)v0;
                        if (v1 == null) {
                            return Unit.INSTANCE;
                        }
                        mentorCheck = v1;
                        if (mentorCheck.isAbandoned()) {
                            return Unit.INSTANCE;
                        }
                        var6_5 = UUID.randomUUID().toString();
                        Intrinsics.checkNotNullExpressionValue((Object)var6_5, (String)"toString(...)");
                        playerMsgId = var6_5;
                        playerMsg = new Ref.ObjectRef();
                        playerMsg.element = new ChatMessage((String)playerMsgId, this.$messageText, true, currentDay, MessageStatus.SENT);
                        var7_6 = GameViewModel.access$get_gameState$p(this.this$0);
                        var8_7 = this.$mentorId;
                        $i$f$update\3\1346 = false;
                        do {
                            prevValue\3 = $this$update\3.getValue();
                            current\4 = (GameState)prevValue\3;
                            $i$a$-update-GameViewModel$processPlayerMessage$1$1\4\2813\0 = false;
                            var13_18 = current\4.getMentors();
                            var14_19 = null;
                            var15_21 = 0.0;
                            var17_26 = null;
                            var18_29 = false;
                            var19_31 = false;
                            var20_34 = null;
                            var21_36 = null;
                            var22_38 = 0.0;
                            var24_40 = 0;
                            var25_42 = null;
                            var26_45 = null;
                            var27_48 = null;
                            var28_50 = null;
                            var29_53 = 0;
                            var30_57 = 0;
                            var31_60 = null;
                            var32_64 = 0;
                            var33_67 = null;
                            var34_68 = null;
                            var35_70 = null;
                            var36_73 = null;
                            var37_75 = null;
                            var38_77 = null;
                            var39_81 = null;
                            var40_83 = null;
                            var41_86 = null;
                            var42_87 = null;
                            var43_89 = 0;
                            var44_93 = 0;
                            var45_96 = 0;
                            var46_99 = null;
                            var47_102 = null;
                            var48_103 = 0;
                            var49_105 = 0;
                            var50_108 = 0.0;
                            var52_111 = 0;
                            var53_113 = null;
                            var54_116 = current\4;
                            $i$f$map\5\1347 = false;
                            var56_121 = $this$map\5;
                            destination\6 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\5, (int)10));
                            $i$f$mapTo\6\2814 = false;
                            for (T item\6 : $this$mapTo\6) {
                                var61_136 = (Mentor)item\6;
                                var62_139 = destination\6;
                                $i$a$-map-GameViewModel$processPlayerMessage$1$1$1\7\2816\4 = false;
                                var62_139.add(Intrinsics.areEqual((Object)it\7.getId(), (Object)var8_7) != false ? Mentor.copy$default(it\7, null, null, null, null, 0, null, false, CollectionsKt.plus((Collection)it\7.getChatHistory(), (Object)playerMsg.element), false, null, false, false, 3967, null) : it\7);
                            }
                            var62_139 = (List)destination\6;
                        } while (!$this$update\3.compareAndSet(prevValue\3, nextValue\3 = GameState.copy$default(var54_116, var53_113, var52_111, var50_108, var49_105, var48_103, var47_102, var46_99, var45_96, var44_93, var43_89, var42_87, var41_86, var40_83, var39_81, var38_77, var37_75, var36_73, var35_70, var34_68, var33_67, false, var31_60, var30_57, var29_53, var28_50, var27_48, var26_45, var25_42, var24_40, var22_38, var21_36, var20_34, false, false, var17_26, var15_21, var14_19, var62_139, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262111, null)));
                        this.L$0 = SpillingKt.nullOutSpilledVariable((Object)currentState);
                        this.L$1 = SpillingKt.nullOutSpilledVariable((Object)mentorCheck);
                        this.L$2 = playerMsgId;
                        this.L$3 = playerMsg;
                        this.I$0 = currentDay;
                        this.label = 1;
                        v2 = DelayKt.delay((long)500L, (Continuation)((Continuation)this));
                        if (v2 == var99_2) {
                            return var99_2;
                        }
                        ** GOTO lbl110
                    }
                    case 1: {
                        currentDay = this.I$0;
                        playerMsg = (Ref.ObjectRef)this.L$3;
                        playerMsgId = (String)this.L$2;
                        mentorCheck = (Mentor)this.L$1;
                        currentState = (GameState)this.L$0;
                        ResultKt.throwOnFailure((Object)$result);
                        v2 = $result;
lbl110:
                        // 2 sources

                        $this$update\3 = GameViewModel.access$get_gameState$p(this.this$0);
                        var8_7 = this.$mentorId;
                        $i$f$update\8\1354 = false;
                        do {
                            prevValue\8 = $this$update\8.getValue();
                            current\9 = (GameState)prevValue\8;
                            $i$a$-update-GameViewModel$processPlayerMessage$1$2\9\2822\0 = false;
                            playerMsg.element = ChatMessage.copy$default((ChatMessage)playerMsg.element, null, null, false, 0, MessageStatus.DELIVERED, 15, null);
                            $this$map\5 = current\9.getMentors();
                            var14_19 = null;
                            var15_21 = 0.0;
                            var17_26 = null;
                            var18_29 = false;
                            var19_31 = false;
                            var20_34 = null;
                            var21_36 = null;
                            var22_38 = 0.0;
                            var24_40 = 0;
                            var25_42 = null;
                            var26_45 = null;
                            var27_48 = null;
                            var28_50 = null;
                            var29_53 = 0;
                            var30_57 = 0;
                            var31_60 = null;
                            var32_64 = 0;
                            var33_67 = null;
                            var34_68 = null;
                            var35_70 = null;
                            var36_73 = null;
                            var37_75 = null;
                            var38_77 = null;
                            var39_81 = null;
                            var40_83 = null;
                            var41_86 = null;
                            var42_87 = null;
                            var43_89 = 0;
                            var44_93 = 0;
                            var45_96 = 0;
                            var46_99 = null;
                            var47_102 = null;
                            var48_103 = 0;
                            var49_105 = 0;
                            var50_108 = 0.0;
                            var52_111 = 0;
                            var53_113 = null;
                            var54_116 = current\9;
                            $i$f$map\10\1356 = false;
                            $this$mapTo\6 = $this$map\10;
                            destination\11 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\10, (int)10));
                            $i$f$mapTo\11\2823 = false;
                            for (T item\11 : $this$mapTo\11) {
                                it\7 = (Mentor)item\11;
                                var62_139 = destination\11;
                                $i$a$-map-GameViewModel$processPlayerMessage$1$2$1\12\2825\9 = false;
                                if (Intrinsics.areEqual((Object)it\12.getId(), (Object)var8_7)) {
                                    nextValue\3 = it\12.getChatHistory();
                                    var65_145 = 0;
                                    var66_148 = null;
                                    var67_152 = 0;
                                    var68_154 = null;
                                    var69_156 = null;
                                    var70_158 = null;
                                    var71_159 = null;
                                    var72_163 = it\12;
                                    $i$f$map\13\1357 = false;
                                    var74_168 = $this$map\13;
                                    destination\14 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\13, (int)10));
                                    $i$f$mapTo\14\2826 = false;
                                    for (T item\14 : $this$mapTo\14) {
                                        var79_177 = (ChatMessage)item\14;
                                        var80_180 /* !! */  = destination\14;
                                        $i$a$-map-GameViewModel$processPlayerMessage$1$2$1$1\15\2828\12 = false;
                                        var80_180 /* !! */ .add(Intrinsics.areEqual((Object)msg\15.getId(), (Object)playerMsgId) != false ? (ChatMessage)playerMsg.element : msg\15);
                                    }
                                    var80_180 /* !! */  = (List)destination\14;
                                    v3 = Mentor.copy$default((Mentor)var72_163, var71_159, var70_158, var69_156, var68_154, var67_152, var66_148, false, var80_180 /* !! */ , false, null, false, false, 3967, null);
                                } else {
                                    v3 = it\12;
                                }
                                var62_139.add(v3);
                            }
                            var62_139 = (List)destination\11;
                        } while (!$this$update\8.compareAndSet(prevValue\8, (Object)(nextValue\8 = GameState.copy$default(var54_116, var53_113, var52_111, var50_108, var49_105, var48_103, var47_102, var46_99, var45_96, var44_93, var43_89, var42_87, var41_86, var40_83, var39_81, var38_77, var37_75, var36_73, var35_70, var34_68, var33_67, false, var31_60, var30_57, var29_53, var28_50, var27_48, var26_45, var25_42, var24_40, var22_38, var21_36, var20_34, false, false, var17_26, var15_21, var14_19, var62_139, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262111, null))));
                        updatedState = (GameState)GameViewModel.access$get_gameState$p(this.this$0).getValue();
                        prevValue\8 = updatedState.getMentors();
                        current\9 = this.$mentorId;
                        $i$a$-update-GameViewModel$processPlayerMessage$1$2\9\2822\0 = prevValue\8;
                        $this$map\10 = $i$a$-update-GameViewModel$processPlayerMessage$1$2\9\2822\0.iterator();
                        while ($this$map\10.hasNext()) {
                            var14_19 = (IntentParser.IntentType)$this$map\10.next();
                            it\17 = (Mentor)var14_19;
                            $i$a$-find-GameViewModel$processPlayerMessage$1$mentor$1\17\1362\0 = false;
                            if (!Intrinsics.areEqual((Object)it\17.getId(), (Object)current\9)) continue;
                            v4 = var14_19;
                            ** GOTO lbl211
                        }
                        v4 = null;
lbl211:
                        // 2 sources

                        v5 = $i$f$update\8\1354 = (Mentor)v4;
                        if (v5 == null) {
                            return Unit.INSTANCE;
                        }
                        mentor = v5;
                        parsed = IntentParser.INSTANCE.parseMessage(this.$messageText, mentor.getUnlockedFeatures());
                        responseText = new Ref.ObjectRef();
                        responseText.element = "I'm not sure how to respond to that.";
                        affinityGain = 0;
                        newHasGreeted = new Ref.BooleanRef();
                        newHasGreeted.element = mentor.getHasGreetedToday();
                        newRecentlyAsked = CollectionsKt.toMutableList((Collection)mentor.getRecentlyAskedTopics());
                        var14_19 = parsed.getType();
                        it\17 = processPlayerMessage.WhenMappings.$EnumSwitchMapping$0[var14_19.ordinal()];
                        switch (it\17) {
                            case 1: {
                                if (!mentor.getHasGreetedToday()) {
                                    responseText.element = "Hello there! Good to see you.";
                                    affinityGain = 1;
                                    newHasGreeted.element = true;
                                    break;
                                }
                                responseText.element = "We already said our hellos! What do you need?";
                                break;
                            }
                            case 2: {
                                var17_26 = parsed.getMatchedTopic();
                                v6 = var17_26;
                                if (v6 == null) {
                                    v6 = "";
                                }
                                topic = v6;
                                responseText.element = IntentParser.INSTANCE.getResponseForFeature(this.$mentorId, topic);
                                if (mentor.getRecentlyAskedTopics().contains(topic)) break;
                                affinityGain = 1;
                                newRecentlyAsked.add(topic);
                                if (newRecentlyAsked.size() <= 5) break;
                                newRecentlyAsked.remove(0);
                                break;
                            }
                            case 3: {
                                responseText.element = "Could you rephrase that? I'm busy with other matters.";
                                affinityGain = -1;
                                break;
                            }
                            default: {
                                throw new NoWhenBranchMatchedException();
                            }
                        }
                        newAffinity = mentor.getHiddenAffinity() + affinityGain;
                        if (newAffinity <= -5) {
                            it\17 = GameViewModel.access$get_gameState$p(this.this$0);
                            var16_186 = this.$mentorId;
                            $i$f$update\18\1398 = false;
                            do {
                                prevValue\18 = $this$update\18.getValue();
                                current\19 = (GameState)prevValue\18;
                                $i$a$-update-GameViewModel$processPlayerMessage$1$3\19\2835\0 = false;
                                var21_36 = current\19.getMentors();
                                var22_39 = null;
                                var23_187 = 0.0;
                                var25_42 = null;
                                var26_46 = false;
                                var27_49 = false;
                                var28_50 = null;
                                var29_54 = null;
                                var30_58 = 0.0;
                                var32_64 = 0;
                                var33_67 = null;
                                var34_68 = null;
                                var35_70 = null;
                                var36_73 = null;
                                var37_76 = 0;
                                var38_78 = 0;
                                var39_81 = null;
                                var40_84 = false;
                                var41_86 = null;
                                var42_87 = null;
                                var43_90 = null;
                                var44_94 = null;
                                var45_97 = null;
                                var46_99 = null;
                                var47_102 = null;
                                var48_104 = null;
                                var49_106 = null;
                                var50_109 = null;
                                var51_189 = 0;
                                var52_111 = 0;
                                var53_114 = 0;
                                var54_116 = null;
                                var55_119 = null;
                                var56_122 = 0;
                                var57_125 = 0;
                                var58_127 = 0.0;
                                var60_133 = 0;
                                var61_136 = null;
                                var62_139 = current\19;
                                $i$f$map\20\1399 = false;
                                $this$map\13 = $this$map\20;
                                destination\21 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\20, (int)10));
                                $i$f$mapTo\21\2836 = false;
                                for (T item\21 : $this$mapTo\21) {
                                    var69_156 = (Mentor)item\21;
                                    var70_158 = destination\21;
                                    $i$a$-map-GameViewModel$processPlayerMessage$1$3$1\22\2838\19 = false;
                                    var70_158.add(Intrinsics.areEqual((Object)it\22.getId(), (Object)var16_186) != false ? Mentor.copy$default((Mentor)it\22, null, null, null, null, newAffinity, null, false, null, false, null, true, false, 3055, null) : it\22);
                                }
                                var70_158 = (List)destination\21;
                            } while (!$this$update\18.compareAndSet(prevValue\18, (Object)(nextValue\18 = GameState.copy$default((GameState)var62_139, (InventoryMethod)var61_136, var60_133, var58_127, var57_125, var56_122, var55_119, (PlayerSkills)var54_116, var53_114, var52_111, var51_189, var50_109, var49_106, var48_104, var47_102, var46_99, var45_97, var44_94, var43_90, var42_87, var41_86, false, var39_81, var38_78, var37_76, var36_73, var35_70, var34_68, var33_67, var32_64, var30_58, var29_54, var28_50, false, false, var25_42, var23_187, var22_39, (List)var70_158, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262111, null))));
                            return Unit.INSTANCE;
                        }
                        baseDelay = 1000L;
                        lengthDelay = (long)((String)responseText.element).length() * 20L;
                        affinityPenalty = newAffinity < 0 ? (long)Math.abs(newAffinity) * 1000L : 0L;
                        typingDelay = baseDelay + lengthDelay + affinityPenalty;
                        var23_188 = GameViewModel.access$get_gameState$p(this.this$0);
                        var24_41 /* !! */  = this.$mentorId;
                        $i$f$update\23\1411 = false;
                        do {
                            prevValue\23 = $this$update\23.getValue();
                            current\24 = (GameState)prevValue\23;
                            $i$a$-update-GameViewModel$processPlayerMessage$1$4\24\2844\0 = false;
                            playerMsg.element = ChatMessage.copy$default((ChatMessage)playerMsg.element, null, null, false, 0, MessageStatus.READ, 15, null);
                            var29_55 = current\24.getMentors();
                            var30_59 = null;
                            var31_61 = 0.0;
                            var33_67 = null;
                            var34_69 = false;
                            var35_71 = false;
                            var36_73 = null;
                            var37_75 = null;
                            var38_79 = 0.0;
                            var40_85 = 0;
                            var41_86 = null;
                            var42_87 = null;
                            var43_91 = null;
                            var44_95 = null;
                            var45_96 = 0;
                            var46_100 = 0;
                            var47_102 = null;
                            var48_103 = 0;
                            var49_107 = null;
                            var50_110 = null;
                            var51_190 = null;
                            var52_112 = null;
                            var53_113 = null;
                            var54_116 = null;
                            var55_120 = null;
                            var56_121 = null;
                            var57_124 = null;
                            var58_128 = null;
                            var59_130 = 0;
                            var60_134 = 0;
                            var61_137 = 0;
                            var62_139 = null;
                            var63_141 = null;
                            var64_143 = 0;
                            var65_145 = 0;
                            var66_150 = 0.0;
                            var68_155 = 0;
                            var69_156 = null;
                            var70_158 = current\24;
                            $i$f$map\25\1413 = false;
                            nextValue\18 = $this$map\25;
                            destination\26 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\25, (int)10));
                            $i$f$mapTo\26\2845 = false;
                            for (T item\26 : $this$mapTo\26) {
                                var77_175 = (Mentor)item\26;
                                var78_176 = destination\26;
                                $i$a$-map-GameViewModel$processPlayerMessage$1$4$1\27\2847\24 = false;
                                if (Intrinsics.areEqual((Object)it\27.getId(), (Object)var24_41 /* !! */ )) {
                                    var80_180 /* !! */  = it\27.getChatHistory();
                                    $i$a$-map-GameViewModel$processPlayerMessage$1$2$1$1\15\2828\12 = false;
                                    var82_183 = null;
                                    var83_191 = 0;
                                    var84_192 = null;
                                    var85_193 = null;
                                    var86_194 = null;
                                    var87_195 = null;
                                    var88_196 = it\27;
                                    $i$f$map\28\1415 = false;
                                    var90_198 = $this$map\28;
                                    destination\29 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\28, (int)10));
                                    $i$f$mapTo\29\2848 = false;
                                    for (T item\29 : $this$mapTo\29) {
                                        var95_203 = (ChatMessage)item\29;
                                        var96_204 = destination\29;
                                        $i$a$-map-GameViewModel$processPlayerMessage$1$4$1$1\30\2850\27 = false;
                                        var96_204.add((ChatMessage)(Intrinsics.areEqual((Object)msg\30.getId(), (Object)playerMsgId) != false ? (ChatMessage)playerMsg.element : msg\30));
                                    }
                                    var96_204 = (List)destination\29;
                                    v7 = Mentor.copy$default((Mentor)var88_196, var87_195, var86_194, var85_193, var84_192, var83_191, var82_183, false, var96_204, false, null, false, true, 1919, null);
                                } else {
                                    v7 = it\27;
                                }
                                var78_176.add(v7);
                            }
                            var78_176 = (List)destination\26;
                        } while (!$this$update\23.compareAndSet(prevValue\23, (Object)(nextValue\23 = GameState.copy$default((GameState)var70_158, (InventoryMethod)var69_156, var68_155, var66_150, var65_145, var64_143, var63_141, (PlayerSkills)var62_139, var61_137, var60_134, var59_130, var58_128, var57_124, (NewsEvent)var56_121, var55_120, (List)var54_116, var53_113, var52_112, var51_190, var50_110, var49_107, false, var47_102, var46_100, var45_96, var44_95, var43_91, var42_87, var41_86, var40_85, var38_79, var37_75, var36_73, false, false, var33_67, var31_61, (List)var30_59, (List)var78_176, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262111, null))));
                        this.L$0 = SpillingKt.nullOutSpilledVariable((Object)currentState);
                        this.L$1 = SpillingKt.nullOutSpilledVariable((Object)mentorCheck);
                        this.L$2 = SpillingKt.nullOutSpilledVariable((Object)playerMsgId);
                        this.L$3 = SpillingKt.nullOutSpilledVariable((Object)playerMsg);
                        this.L$4 = SpillingKt.nullOutSpilledVariable((Object)updatedState);
                        this.L$5 = SpillingKt.nullOutSpilledVariable((Object)mentor);
                        this.L$6 = SpillingKt.nullOutSpilledVariable((Object)parsed);
                        this.L$7 = responseText;
                        this.L$8 = newHasGreeted;
                        this.L$9 = newRecentlyAsked;
                        this.I$0 = currentDay;
                        this.I$1 = affinityGain;
                        this.I$2 = newAffinity;
                        this.J$0 = baseDelay;
                        this.J$1 = lengthDelay;
                        this.J$2 = affinityPenalty;
                        this.J$3 = typingDelay;
                        this.label = 2;
                        v8 = DelayKt.delay((long)typingDelay, (Continuation)((Continuation)this));
                        if (v8 == var99_2) {
                            return var99_2;
                        }
                        ** GOTO lbl453
                    }
                    case 2: {
                        typingDelay = this.J$3;
                        affinityPenalty = this.J$2;
                        lengthDelay = this.J$1;
                        baseDelay = this.J$0;
                        newAffinity = this.I$2;
                        affinityGain = this.I$1;
                        currentDay = this.I$0;
                        newRecentlyAsked = (List)this.L$9;
                        newHasGreeted = (Ref.BooleanRef)this.L$8;
                        responseText = (Ref.ObjectRef)this.L$7;
                        parsed = (IntentParser.ParsedIntent)this.L$6;
                        mentor = (Mentor)this.L$5;
                        updatedState = (GameState)this.L$4;
                        playerMsg = (Ref.ObjectRef)this.L$3;
                        playerMsgId = (String)this.L$2;
                        mentorCheck = (Mentor)this.L$1;
                        currentState = (GameState)this.L$0;
                        ResultKt.throwOnFailure((Object)$result);
                        v8 = $result;
lbl453:
                        // 2 sources

                        $this$update\23 = GameViewModel.access$get_gameState$p(this.this$0);
                        var24_41 /* !! */  = this.this$0;
                        var25_44 = this.$mentorId;
                        $i$f$update\31\1423 = false;
                        do {
                            prevValue\31 = $this$update\31.getValue();
                            current\32 = (GameState)prevValue\31;
                            $i$a$-update-GameViewModel$processPlayerMessage$1$5\32\2857\0 = false;
                            var30_59 = current\32.getMentors();
                            for (T var32_65 : var30_59) {
                                it\33 = (Mentor)var32_65;
                                $i$a$-find-GameViewModel$processPlayerMessage$1$5$m$1\33\1424\32 = false;
                                if (!Intrinsics.areEqual((Object)it\33.getId(), (Object)var25_44)) continue;
                                v9 = var32_65;
                                ** GOTO lbl470
                            }
                            v9 = null;
lbl470:
                            // 2 sources

                            if ((Mentor)v9 == null) {
                                v10 = current\32;
                                continue;
                            }
                            v11 = newBuffUnlocked\32 = m\32.isBuffUnlocked() != false || newAffinity >= 50;
                            if (newBuffUnlocked\32 && !m\32.isBuffUnlocked()) {
                                GameViewModel.access$get_snackBarMessage$p(var24_41 /* !! */ ).setValue((Object)("\ud83c\udf89 " + m\32.getName() + " respects you enough to unlock their passive buff!"));
                            }
                            mentorMsg\32 = new ChatMessage(null, (String)responseText.element, false, current\32.getDay(), null, 17, null);
                            var30_59 = current\32.getMentors();
                            var38_80 = null;
                            var39_82 = 0.0;
                            var41_86 = null;
                            var42_88 = false;
                            var43_92 = false;
                            var44_95 = null;
                            var45_98 = null;
                            var46_101 = 0.0;
                            var48_103 = 0;
                            var49_107 = null;
                            var50_110 = null;
                            var51_190 = null;
                            var52_112 = null;
                            var53_115 = 0;
                            var54_117 = 0;
                            var55_120 = null;
                            var56_123 = false;
                            var57_124 = null;
                            var58_128 = null;
                            var59_131 = null;
                            var60_135 = null;
                            var61_138 = null;
                            var62_139 = null;
                            var63_141 = null;
                            var64_144 = null;
                            var65_147 = null;
                            var66_151 = null;
                            var67_152 = 0;
                            var68_155 = 0;
                            var69_157 = 0;
                            var70_158 = null;
                            var71_162 = null;
                            var72_164 = 0;
                            var73_167 = 0;
                            var74_170 = 0.0;
                            var76_174 = 0;
                            var77_175 = null;
                            var78_176 = current\32;
                            $i$f$map\34\1432 = false;
                            var32_66 = $this$map\34;
                            destination\35 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\34, (int)10));
                            $i$f$mapTo\35\2858 = false;
                            for (T item\35 : $this$mapTo\35) {
                                $i$a$-map-GameViewModel$processPlayerMessage$1$2$1$1\15\2828\12 = (Mentor)item\35;
                                var82_183 = destination\35;
                                $i$a$-map-GameViewModel$processPlayerMessage$1$5$1\36\2860\32 = false;
                                var82_183.add((void)(Intrinsics.areEqual((Object)it\36.getId(), (Object)var25_44) != false ? Mentor.copy$default((Mentor)it\36, null, null, null, null, newAffinity, null, newBuffUnlocked\32 != false, CollectionsKt.plus((Collection)it\36.getChatHistory(), (Object)mentorMsg\32), newHasGreeted.element, newRecentlyAsked, false, false, 1071, null) : it\36));
                            }
                            var82_183 = (List)destination\35;
                            v10 = GameState.copy$default((GameState)var78_176, var77_175, var76_174, var74_170, var73_167, var72_164, var71_162, var70_158, var69_157, var68_155, var67_152, var66_151, var65_147, var64_144, var63_141, var62_139, var61_138, var60_135, var59_131, var58_128, var57_124, false, var55_120, var54_117, var53_115, var52_112, var51_190, var50_110, var49_107, var48_103, var46_101, var45_98, var44_95, false, false, var41_86, var39_82, var38_80, var82_183, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262111, null);
                        } while (!$this$update\31.compareAndSet(prevValue\31, (Object)(nextValue\31 = v10)));
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            public final Continuation<Unit> create(Object value, Continuation<?> $completion) {
                return (Continuation)new /* invalid duplicate definition of identical inner class */;
            }

            public final Object invoke(CoroutineScope p1, Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        }), (int)3, null);
    }

    /*
     * WARNING - void declaration
     */
    public final void sendApologyGift(@NotNull String mentorId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)mentorId, (String)"mentorId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Collection<Mentor> collection;
            void $this$mapTo\5;
            void $this$map\4;
            Mentor mentor;
            Object v0;
            Object object2;
            GameState gameState3;
            block6: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getMentors();
                for (Object t : iterable) {
                    object2 = (Mentor)t;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)((Mentor)object2).getId(), (Object)mentorId)) continue;
                    v0 = t;
                    break block6;
                }
                v0 = null;
            }
            if ((Mentor)v0 == null) {
                gameState = gameState3;
                continue;
            }
            if (!mentor.isAbandoned()) {
                gameState = gameState3;
                continue;
            }
            double d = 500.0;
            if (gameState3.getCash() < d) {
                this._snackBarMessage.setValue((Object)"You can't afford a $500 apology gift.");
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("Sent a premium apology gift to " + mentor.getName() + "."));
            ChatMessage chatMessage = new ChatMessage(null, "\ud83c\udf81 You sent a premium Apology Basket.", true, gameState3.getDay(), MessageStatus.READ, 1, null);
            ChatMessage chatMessage2 = new ChatMessage(null, "I received your gift. Let's start fresh. But don't waste my time again.", false, gameState3.getDay(), null, 17, null);
            object2 = CollectionsKt.plus((Collection)CollectionsKt.plus((Collection)mentor.getChatHistory(), (Object)chatMessage), (Object)chatMessage2);
            Mentor mentor2 = Mentor.copy$default(mentor, null, null, null, null, 0, null, false, (List)object2, false, null, false, false, 2927, null);
            object2 = gameState3.getMentors();
            List list = null;
            double d2 = 0.0;
            EndgameChoice endgameChoice = null;
            boolean bl4 = false;
            boolean bl5 = false;
            Set set = null;
            Map map = null;
            double d3 = 0.0;
            int n = 0;
            List list2 = null;
            List list3 = null;
            List list4 = null;
            List list5 = null;
            int n2 = 0;
            int n3 = 0;
            String string = null;
            boolean bl6 = false;
            DailyReport dailyReport = null;
            List list6 = null;
            Map map2 = null;
            BankState bankState = null;
            Map map3 = null;
            List list7 = null;
            List list8 = null;
            NewsEvent newsEvent = null;
            LifetimeStats lifetimeStats = null;
            List list9 = null;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            PlayerSkills playerSkills = null;
            Set set2 = null;
            int n7 = 0;
            int n8 = 0;
            double d4 = gameState3.getCash() - d;
            int n9 = 0;
            InventoryMethod inventoryMethod = null;
            GameState gameState4 = gameState3;
            boolean bl7 = false;
            void var56_52 = $this$map\4;
            Collection collection2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\4, (int)10));
            boolean bl8 = false;
            for (Object t : $this$mapTo\5) {
                void it\6;
                Mentor mentor3 = (Mentor)t;
                collection = collection2;
                boolean bl9 = false;
                collection.add((Mentor)(Intrinsics.areEqual((Object)it\6.getId(), (Object)mentorId) ? mentor2 : it\6));
            }
            collection = (List)collection2;
            gameState = GameState.copy$default(gameState4, inventoryMethod, n9, d4, n8, n7, set2, playerSkills, n6, n5, n4, list9, lifetimeStats, newsEvent, list8, list7, map3, bankState, map2, list6, dailyReport, bl6, string, n3, n2, list5, list4, list3, list2, n, d3, map, set, bl5, bl4, endgameChoice, d2, list, collection, null, false, null, null, null, null, null, null, false, null, null, null, -5, 262111, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void hireExecutive(@NotNull String executiveId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)executiveId, (String)"executiveId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$mapTo\5;
            Object v0;
            Object object22;
            GameState gameState3;
            block5: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getExecutives();
                for (Object object22 : iterable) {
                    Executive executive = (Executive)object22;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)executive.getId(), (Object)executiveId)) continue;
                    v0 = object22;
                    break block5;
                }
                v0 = null;
            }
            Executive executive = v0;
            if (executive == null || executive.isHired()) {
                gameState = gameState3;
                continue;
            }
            if (gameState3.getCash() < executive.getHiringCost()) {
                this._snackBarMessage.setValue((Object)("Insufficient funds to hire " + executive.getName() + "."));
                gameState = gameState3;
                continue;
            }
            Iterable iterable = gameState3.getExecutives();
            boolean bl4 = false;
            object22 = iterable;
            Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
            boolean bl5 = false;
            for (Object t : $this$mapTo\5) {
                void it\6;
                Executive executive2 = (Executive)t;
                Collection collection2 = collection;
                boolean bl6 = false;
                collection2.add(Intrinsics.areEqual((Object)it\6.getId(), (Object)executiveId) ? Executive.copy$default((Executive)it\6, null, null, null, 0.0, 0.0, null, true, null, 191, null) : it\6);
            }
            List list = (List)collection;
            String string = executive.getName();
            ExecutiveRole executiveRole = executive.getRole();
            String string2 = "%,.2f";
            object22 = new Object[]{executive.getHiringCost()};
            String string3 = String.format(string2, Arrays.copyOf(object22, ((T)object22).length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Hired " + string + " (" + executiveRole + ") for $" + string3 + "!"));
            List list2 = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83e\udd1d Hired " + executive.getRole() + ": " + executive.getName() + " joined the executive board.")), (Iterable)CollectionsKt.take((Iterable)gameState3.getDailyLogs(), (int)19));
            double d = gameState3.getCash() - executive.getHiringCost();
            gameState = GameState.copy$default(gameState3, null, 0, d, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list2, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, list, null, null, false, null, null, null, null, null, null, false, null, null, null, -262149, 262127, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void acceptContract(@NotNull String offerId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)offerId, (String)"offerId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterTo\6;
            RivalCompany rivalCompany;
            Object v2;
            Object object2;
            ContractOffer contractOffer;
            GameState gameState3;
            block7: {
                Object object3;
                Object object4;
                Object object52;
                block6: {
                    object = mutableStateFlow.getValue();
                    gameState3 = (GameState)object;
                    boolean bl2 = false;
                    Iterable iterable = gameState3.getPendingContractOffers();
                    for (Object object52 : iterable) {
                        object4 = (ContractOffer)object52;
                        boolean bl3 = false;
                        if (!Intrinsics.areEqual((Object)((ContractOffer)object4).getId(), (Object)offerId)) continue;
                        object3 = object52;
                        break block6;
                    }
                    object3 = null;
                }
                contractOffer = (ContractOffer)object3;
                if (contractOffer == null) {
                    this._snackBarMessage.setValue((Object)"Contract offer is no longer active.");
                    gameState = gameState3;
                    continue;
                }
                object52 = gameState3.getRivalCompanies();
                object4 = object52.iterator();
                while (object4.hasNext()) {
                    Object bl3 = object4.next();
                    object2 = (RivalCompany)bl3;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)((RivalCompany)object2).getId(), (Object)contractOffer.getRivalId())) continue;
                    v2 = bl3;
                    break block7;
                }
                v2 = null;
            }
            if ((rivalCompany = (RivalCompany)v2) == null) {
                rivalCompany = RivalCatalog.INSTANCE.getRivalById(contractOffer.getRivalId());
            }
            RivalCompany rivalCompany2 = rivalCompany;
            Product product = ProductCatalog.INSTANCE.getById(contractOffer.getTargetProduct());
            ContractOffer contractOffer2 = ContractOffer.copy$default(contractOffer, null, null, null, 0, 0.0, 0, true, 0, 0.0, 0, 0.0, 0, 0, 0.0, 0, false, 65471, null);
            Iterable iterable = gameState3.getPendingContractOffers();
            boolean bl5 = false;
            object2 = iterable;
            Collection collection = new ArrayList();
            boolean bl6 = false;
            for (Object t : $this$filterTo\6) {
                ContractOffer contractOffer3 = (ContractOffer)t;
                boolean bl7 = false;
                if (!(!Intrinsics.areEqual((Object)contractOffer3.getId(), (Object)offerId))) continue;
                collection.add(t);
            }
            List list = (List)collection;
            List list2 = CollectionsKt.plus((Collection)gameState3.getActiveContracts(), (Object)contractOffer2);
            this._snackBarMessage.setValue((Object)("\ud83e\udd1d Contract Executed with " + rivalCompany2.getName() + "! Quota: " + contractOffer.getRequiredQuantity() + "x " + product.getName() + "/day."));
            String string = rivalCompany2.getName();
            int n = contractOffer.getRequiredQuantity();
            String string2 = product.getName();
            String string3 = "%.2f";
            Object[] objectArray = new Object[]{contractOffer.getPayoutAmount()};
            String string4 = String.format(string3, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string4, (String)"format(...)");
            List list3 = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83e\udd1d Executed B2B Contract with " + string + ": " + n + "x " + string2 + " @ $" + string4 + "/day for " + contractOffer.getDurationDays() + " days.")), (Iterable)CollectionsKt.take((Iterable)gameState3.getDailyLogs(), (int)19));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list3, null, false, null, 0, 0, null, null, list, list2, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -201588737, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void declineContract(@NotNull String offerId) {
        List list;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)offerId, (String)"offerId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterTo\7;
            RivalCompany rivalCompany;
            Object v0;
            Object object2;
            block7: {
                object = mutableStateFlow.getValue();
                gameState = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState.getPendingContractOffers();
                for (Object t : iterable) {
                    object2 = (ContractOffer)t;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)((ContractOffer)object2).getId(), (Object)offerId)) continue;
                    v0 = t;
                    break block7;
                }
                v0 = null;
            }
            ContractOffer contractOffer = v0;
            if (contractOffer != null) {
                Object v1;
                block8: {
                    boolean bl4 = false;
                    object2 = gameState.getRivalCompanies();
                    Iterator bl3 = object2.iterator();
                    while (bl3.hasNext()) {
                        ContractOffer contractOffer2;
                        Object t = bl3.next();
                        RivalCompany rivalCompany2 = (RivalCompany)t;
                        boolean bl5 = false;
                        if (!Intrinsics.areEqual((Object)rivalCompany2.getId(), (Object)contractOffer2.getRivalId())) continue;
                        v1 = t;
                        break block8;
                    }
                    v1 = null;
                }
                rivalCompany = v1;
            } else {
                rivalCompany = null;
            }
            RivalCompany rivalCompany3 = rivalCompany;
            Iterable iterable = gameState.getPendingContractOffers();
            boolean bl6 = false;
            object2 = iterable;
            Collection collection = new ArrayList();
            boolean bl7 = false;
            for (Object t : $this$filterTo\7) {
                ContractOffer contractOffer3 = (ContractOffer)t;
                boolean bl8 = false;
                if (!(!Intrinsics.areEqual((Object)contractOffer3.getId(), (Object)offerId))) continue;
                collection.add(t);
            }
            list = (List)collection;
            Object object3 = rivalCompany3;
            if (object3 == null || (object3 = ((RivalCompany)object3).getName()) == null) {
                object3 = "rival";
            }
            this._snackBarMessage.setValue((Object)("Contract offer from " + (String)object3 + " declined."));
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, list, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -67108865, 262143, null))));
    }

    /*
     * WARNING - void declaration
     */
    public final void bargainContract(@NotNull String offerId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)offerId, (String)"offerId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterTo\9;
            Iterable iterable;
            RivalCompany rivalCompany;
            Object v2;
            ContractOffer contractOffer;
            GameState gameState3;
            block9: {
                Object object2;
                Object object3;
                Object object42;
                block8: {
                    object = mutableStateFlow.getValue();
                    gameState3 = (GameState)object;
                    boolean bl2 = false;
                    Iterable iterable2 = gameState3.getPendingContractOffers();
                    for (Object object42 : iterable2) {
                        object3 = (ContractOffer)object42;
                        boolean bl3 = false;
                        if (!Intrinsics.areEqual((Object)((ContractOffer)object3).getId(), (Object)offerId)) continue;
                        object2 = object42;
                        break block8;
                    }
                    object2 = null;
                }
                contractOffer = (ContractOffer)object2;
                if (contractOffer == null) {
                    this._snackBarMessage.setValue((Object)"Contract offer is no longer active.");
                    gameState = gameState3;
                    continue;
                }
                object42 = gameState3.getRivalCompanies();
                object3 = object42.iterator();
                while (object3.hasNext()) {
                    Object bl3 = object3.next();
                    RivalCompany rivalCompany2 = (RivalCompany)bl3;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)rivalCompany2.getId(), (Object)contractOffer.getRivalId())) continue;
                    v2 = bl3;
                    break block9;
                }
                v2 = null;
            }
            if ((rivalCompany = (RivalCompany)v2) == null) {
                rivalCompany = RivalCatalog.INSTANCE.getRivalById(contractOffer.getRivalId());
            }
            RivalCompany rivalCompany3 = rivalCompany;
            int n = gameState3.getPlayerSkills().getSilverTongueLevel();
            int n2 = RangesKt.coerceAtMost((int)(50 + n * 15), (int)95);
            int n3 = Random.Default.nextInt(1, 101);
            if (n3 <= n2) {
                void $this$mapTo\6;
                double d = (double)((int)(contractOffer.getPayoutAmount() * 1.15 * 100.0)) / 100.0;
                ContractOffer contractOffer2 = ContractOffer.copy$default(contractOffer, null, null, null, 0, d, 0, false, 0, 0.0, contractOffer.getBargainCount() + 1, 0.0, 0, 0, 0.0, 0, false, 65007, null);
                Iterable iterable3 = gameState3.getPendingContractOffers();
                boolean $i$f$map\5\15782 = false;
                Object object5 = iterable3;
                Object[] objectArray = (Object[])new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable3, (int)10));
                boolean bl5 = false;
                for (Object t : $this$mapTo\6) {
                    void it\7;
                    ContractOffer contractOffer3 = (ContractOffer)t;
                    Object[] objectArray2 = objectArray;
                    boolean bl6 = false;
                    objectArray2.add(Intrinsics.areEqual((Object)it\7.getId(), (Object)offerId) ? contractOffer2 : it\7);
                }
                iterable = (List)objectArray;
                String string = rivalCompany3.getName();
                String $i$f$map\5\15782 = "%.2f";
                object5 = new Object[]{d};
                String string2 = String.format($i$f$map\5\15782, Arrays.copyOf(object5, ((Object[])object5).length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("\ud83c\udfb2 Bargain Successful! Silver Tongue charmed " + string + ". Payout raised +15% to $" + string2 + "/day!"));
                String string3 = rivalCompany3.getName();
                object5 = "%.2f";
                objectArray = new Object[]{d};
                String string4 = String.format((String)object5, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string4, (String)"format(...)");
                List list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83c\udfb2 Hardball Negotiation: Charmed " + string3 + ", boosting contract daily payout to $" + string4 + ".")), (Iterable)CollectionsKt.take((Iterable)gameState3.getDailyLogs(), (int)19));
                gameState = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list, null, false, null, 0, 0, null, null, (List)iterable, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -67371009, 262143, null);
                continue;
            }
            Iterable iterable4 = gameState3.getPendingContractOffers();
            boolean bl7 = false;
            iterable = iterable4;
            Collection collection = new ArrayList();
            boolean bl8 = false;
            for (Object t : $this$filterTo\9) {
                ContractOffer contractOffer4 = (ContractOffer)t;
                boolean bl9 = false;
                if (!(!Intrinsics.areEqual((Object)contractOffer4.getId(), (Object)offerId))) continue;
                collection.add(t);
            }
            List updatedPending\3 = (List)collection;
            this._snackBarMessage.setValue((Object)("\ud83c\udfb2 Bargain Failed! " + rivalCompany3.getName() + " was insulted by your aggressive terms and revoked the offer!"));
            List list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\u26a0\ufe0f Deal Collapsed: " + rivalCompany3.getName() + " was insulted during negotiation and revoked their RFP offer.")), (Iterable)CollectionsKt.take((Iterable)gameState3.getDailyLogs(), (int)19));
            gameState = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list, null, false, null, 0, 0, null, null, updatedPending\3, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -67371009, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void upgradePlayerSkill(@NotNull SkillType skillType) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)((Object)skillType), (String)"skillType");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            PlayerSkills playerSkills;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            PlayerSkills playerSkills2 = gameState3.getPlayerSkills();
            int n = playerSkills2.getLevel(skillType);
            if (n >= skillType.getMaxLevel()) {
                this._snackBarMessage.setValue((Object)(skillType.getTitle() + " is already at max level!"));
                gameState = gameState3;
                continue;
            }
            int n2 = skillType.costForLevel(n);
            if (gameState3.getResearchPoints() < n2) {
                this._snackBarMessage.setValue((Object)("Insufficient RP. Requires " + n2 + " RP (Have " + gameState3.getResearchPoints() + " RP)."));
                gameState = gameState3;
                continue;
            }
            int n3 = n + 1;
            switch (WhenMappings.$EnumSwitchMapping$1[skillType.ordinal()]) {
                case 1: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, 0, 0, n3, 0, 0, 0, 119, null);
                    break;
                }
                case 2: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, n3, 0, 0, 0, 0, 0, 0, 126, null);
                    break;
                }
                case 3: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, n3, 0, 0, 0, 0, 0, 125, null);
                    break;
                }
                case 4: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, 0, n3, 0, 0, 0, 0, 123, null);
                    break;
                }
                case 5: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, 0, 0, 0, n3, 0, 0, 111, null);
                    break;
                }
                case 6: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, 0, 0, 0, 0, n3, 0, 95, null);
                    break;
                }
                case 7: {
                    playerSkills = PlayerSkills.copy$default(playerSkills2, 0, 0, 0, 0, 0, 0, n3, 63, null);
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            PlayerSkills playerSkills3 = playerSkills;
            int n4 = skillType == SkillType.STAMINA ? 1 : 0;
            this._snackBarMessage.setValue((Object)("Upgraded " + skillType.getTitle() + " to Level " + n3 + "! \u2728"));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, 0.0, 0, gameState3.getResearchPoints() - n2, null, playerSkills3, gameState3.getDailyActionsRemaining() + n4, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -209, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * Unable to fully structure code
     */
    private final ActiveProject tryAssignProject(GameState current, ActiveProject baseProject) {
        block9: {
            block8: {
                isResearch = baseProject.getType() == ProjectType.TECH_RESEARCH;
                dedicatedCapacity = isResearch != false ? current.getOwnedLabs() : current.getOwnedConstructionCrews();
                $this$count\1 = current.getActiveProjects();
                $i$f$count\1\1649 = false;
                if (!($this$count\1 instanceof Collection) || !((Collection)$this$count\1).isEmpty()) break block8;
                v0 = 0;
                break block9;
            }
            count\1 = 0;
            for (T element\1 : $this$count\1) {
                it\2 = (ActiveProject)element\1;
                $i$a$-count-GameViewModel$tryAssignProject$currentDedicatedCount$1\2\2925\0 = false;
                if (!it\2.isDedicated()) ** GOTO lbl-1000
                v1 = isResearch ? it\2.getType() == ProjectType.TECH_RESEARCH : it\2.getType() == ProjectType.FACILITY_CONSTRUCTION || it\2.getType() == ProjectType.FACILITY_UPGRADE;
                if (v1) {
                    v2 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v2 = false;
                }
                if (!v2 || ++count\1 >= 0) continue;
                CollectionsKt.throwCountOverflow();
            }
            v0 = currentDedicatedCount = count\1;
        }
        if (currentDedicatedCount < dedicatedCapacity) {
            return ActiveProject.copy$default(baseProject, null, null, null, null, null, 0, 0, 0, 0.0, true, 511, null);
        }
        personalCapacity = current.getPlayerSkills().getPersonalActionCapacity();
        $this$count\3 = current.getActiveProjects();
        $i$f$count\3\1658 = false;
        if ($this$count\3 instanceof Collection && ((Collection)$this$count\3).isEmpty()) {
            v3 = 0;
        } else {
            count\3 = 0;
            for (T element\3 : $this$count\3) {
                it\4 = (ActiveProject)element\3;
                $i$a$-count-GameViewModel$tryAssignProject$currentPersonalCount$1\4\2929\0 = false;
                if (!(it\4.isDedicated() == false) || ++count\3 >= 0) continue;
                CollectionsKt.throwCountOverflow();
            }
            v3 = currentPersonalCount = count\3;
        }
        if (currentPersonalCount < personalCapacity) {
            return ActiveProject.copy$default(baseProject, null, null, null, null, null, 0, 0, 0, 0.0, false, 511, null);
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    public final void unlockTechnology(@NotNull String techId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)techId, (String)"techId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Object object2;
            void $this$filterTo\6;
            Object v1;
            Object object32;
            GameState gameState3;
            block11: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                if (gameState3.getResearchNodeStatuses().get(techId) == NodeStatus.COMPLETED) {
                    this._snackBarMessage.setValue((Object)"Technology is already researched.");
                    gameState = gameState3;
                    continue;
                }
                if (gameState3.isTechUnderResearch(techId)) {
                    this._snackBarMessage.setValue((Object)"R&D research is already underway for this technology!");
                    gameState = gameState3;
                    continue;
                }
                Iterable iterable = ResearchCatalog.INSTANCE.getALL_NODES();
                boolean bl3 = false;
                for (Object object32 : iterable) {
                    ResearchNode researchNode = (ResearchNode)object32;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)researchNode.getId(), (Object)techId)) continue;
                    v1 = object32;
                    break block11;
                }
                v1 = null;
            }
            ResearchNode researchNode = v1;
            if (researchNode == null) {
                this._snackBarMessage.setValue((Object)"Unknown technology.");
                gameState = gameState3;
                continue;
            }
            Iterable iterable = researchNode.getPrerequisites();
            boolean bl5 = false;
            object32 = iterable;
            Iterable iterable2 = new ArrayList();
            boolean bl6 = false;
            for (Object t : $this$filterTo\6) {
                String string = (String)t;
                boolean bl7 = false;
                if (!(gameState3.getResearchNodeStatuses().get(string) != NodeStatus.COMPLETED)) continue;
                iterable2.add(t);
            }
            List list = (List)iterable2;
            if (!((Collection)list).isEmpty()) {
                void $this$mapNotNullTo\9;
                object2 = list;
                boolean bl8 = false;
                iterable2 = object2;
                Collection collection = new ArrayList();
                boolean bl9 = false;
                void $this$forEach\10 = $this$mapNotNullTo\9;
                boolean bl10 = false;
                Iterator iterator = $this$forEach\10.iterator();
                while (iterator.hasNext()) {
                    String string;
                    Object v2;
                    block12: {
                        Object t;
                        Object t2 = t = iterator.next();
                        boolean bl11 = false;
                        String string2 = (String)t2;
                        boolean bl12 = false;
                        Iterable iterable3 = ResearchCatalog.INSTANCE.getALL_NODES();
                        boolean bl13 = false;
                        for (Object t3 : iterable3) {
                            ResearchNode researchNode2 = (ResearchNode)t3;
                            boolean bl14 = false;
                            if (!Intrinsics.areEqual((Object)researchNode2.getId(), (Object)string2)) continue;
                            v2 = t3;
                            break block12;
                        }
                        v2 = null;
                    }
                    ResearchNode researchNode3 = v2;
                    if ((researchNode3 != null ? researchNode3.getTitle() : null) == null) continue;
                    string = string;
                    boolean bl15 = false;
                    collection.add(string);
                }
                String string = CollectionsKt.joinToString$default((Iterable)((List)collection), (CharSequence)", ", null, null, (int)0, null, null, (int)62, null);
                this._snackBarMessage.setValue((Object)("Requires prerequisite research: " + string + " first!"));
                gameState = gameState3;
                continue;
            }
            if (gameState3.getCash() < (double)researchNode.getResearchCost()) {
                int n = researchNode.getResearchCost();
                object2 = "%.2f";
                Object[] bl8 = new Object[]{gameState3.getCash()};
                String string = String.format((String)object2, Arrays.copyOf(bl8, bl8.length));
                Intrinsics.checkNotNullExpressionValue((Object)string, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient Cash. Requires $" + n + " (Have $" + string + ")."));
                gameState = gameState3;
                continue;
            }
            ActiveProject activeProject = new ActiveProject(null, ProjectType.TECH_RESEARCH, techId, researchNode.getTitle(), "\ud83d\udd2c", researchNode.getResearchTimeDays(), researchNode.getResearchTimeDays(), 0, 180.0, false, 641, null);
            ActiveProject activeProject2 = this.tryAssignProject(gameState3, activeProject);
            if (activeProject2 == null) {
                this._snackBarMessage.setValue((Object)"All Slots & Labs Busy! Upgrade Multi-Tasking or build Labs.");
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("Initiated R&D on " + researchNode.getTitle() + "! (" + researchNode.getResearchTimeDays() + " days to finish)."));
            Map map = MapsKt.toMutableMap(gameState3.getResearchNodeStatuses());
            map.put(techId, NodeStatus.RESEARCHING);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() - (double)researchNode.getResearchCost(), 0, 0, null, null, 0, 0, 0, CollectionsKt.plus((Collection)gameState3.getActiveProjects(), (Object)activeProject2), null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, map, -1029, 131071, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void toggleAutoBuy(@NotNull String itemId, boolean isActive) {
        Map map;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)itemId, (String)"itemId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
            map = MapsKt.toMutableMap(gameState.getAutoBuySubscriptions());
            if (isActive) {
                map.put(itemId, true);
                this._snackBarMessage.setValue((Object)("Enabled Auto-Procurement for " + ProductCatalog.INSTANCE.getById(itemId).getName() + "."));
                continue;
            }
            map.remove(itemId);
            this._snackBarMessage.setValue((Object)("Disabled Auto-Procurement for " + ProductCatalog.INSTANCE.getById(itemId).getName() + "."));
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, map, null, null, null, null, false, null, null, null, -1, 261631, null))));
    }

    public final void toggleAutoSell(@NotNull String itemId, boolean isActive) {
        Map map;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)itemId, (String)"itemId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
            map = MapsKt.toMutableMap(gameState.getAutoSellSubscriptions());
            if (isActive) {
                map.put(itemId, true);
                this._snackBarMessage.setValue((Object)("Enabled Auto-Sell for " + ProductCatalog.INSTANCE.getById(itemId).getName() + "."));
                continue;
            }
            map.remove(itemId);
            this._snackBarMessage.setValue((Object)("Disabled Auto-Sell for " + ProductCatalog.INSTANCE.getById(itemId).getName() + "."));
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, map, null, null, null, false, null, null, null, -1, 261119, null))));
    }

    public final void workManualLabor() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Object object2;
            Object[] objectArray;
            String string;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            if (gameState3.getDailyActionsRemaining() <= 0) {
                this._snackBarMessage.setValue((Object)"\u26a1 Out of Daily Actions! Click 'End Day' to rest.");
                gameState = gameState3;
                continue;
            }
            double d = gameState3.getPlayerSkills().getHustlerCashBonus();
            double d2 = 35.0 + (double)gameState3.getReputation() * 0.3 + d;
            if (d > 0.0) {
                string = "%.0f";
                objectArray = new Object[]{d};
                String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                object2 = " (+$" + string2 + " Hustler)";
            } else {
                object2 = "";
            }
            String string3 = object2;
            string = "%.2f";
            objectArray = new Object[]{d2};
            String string4 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string4, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Worked overtime! +$" + string4 + string3 + " (1 Action used)"));
            LifetimeStats lifetimeStats = LifetimeStats.copy$default(gameState3.getStats(), 0, gameState3.getStats().getTotalCashEarned() + d2, 0L, 0L, 0, 0, 0, 0.0, 0L, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0x3FFFFFD, null);
            double d3 = gameState3.getCash() + d2;
            int n = gameState3.getManualLaborCount() + 1;
            int n2 = gameState3.getDailyActionsRemaining() - 1;
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d3, 0, 0, null, null, n2, 0, 0, null, lifetimeStats, null, null, null, null, null, null, null, null, false, null, 0, n, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -8390789, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void studyManualResearch() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            if (gameState3.getDailyActionsRemaining() <= 0) {
                this._snackBarMessage.setValue((Object)"\u26a1 Out of Daily Actions! Click 'End Day' to rest.");
                gameState = gameState3;
                continue;
            }
            int n = 4;
            this._snackBarMessage.setValue((Object)("Studied dairy biochemistry! +" + n + " Research Points (1 Action used)"));
            LifetimeStats lifetimeStats = LifetimeStats.copy$default(gameState3.getStats(), 0, 0.0, 0L, 0L, 0, 0, gameState3.getStats().getTotalResearchPointsEarned() + n, 0.0, 0L, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0x3FFFFBF, null);
            gameState = GameState.copy$default(gameState3, null, 0, 0.0, 0, gameState3.getResearchPoints() + n, null, null, gameState3.getDailyActionsRemaining() - 1, 0, 0, null, lifetimeStats, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -2193, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void sellProduct(@NotNull String productType, int amount) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)productType, (String)"productType");
        if (amount <= 0) {
            return;
        }
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            MarketItemState marketItemState;
            int n;
            void n2;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            boolean bl3 = Intrinsics.areEqual((Object)productType, (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId());
            Iterable iterable = gameState3.getInventory();
            boolean $i$f$filter\3\18242 = false;
            Iterable iterable2 = iterable;
            Collection collection = new ArrayList();
            boolean bl4 = false;
            for (Object t : n2) {
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                boolean bl5 = false;
                if (!(Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)productType) && (bl3 || !inventoryBatch.isSpoiled()))) continue;
                collection.add(t);
            }
            List list = (List)collection;
            Iterable $i$f$filter\3\18242 = list;
            int n2 = 0;
            for (Object t : $i$f$filter\3\18242) {
                void it\6;
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                int n3 = n2;
                boolean bl6 = false;
                int n4 = it\6.getQuantity();
                n2 = n3 + n4;
            }
            int n5 = n2;
            if (n5 <= 0) {
                this._snackBarMessage.setValue((Object)"No unspoiled stock available to sell.");
                gameState = gameState3;
                continue;
            }
            int n6 = n = RangesKt.coerceAtMost((int)amount, (int)n5);
            double d = 0.0;
            MarketItemState marketItemState2 = marketItemState = gameState3.getMarketPrices().get(productType);
            double d2 = marketItemState2 != null ? marketItemState2.getCurrentPrice() : ProductCatalog.INSTANCE.getById(productType).getBasePrice();
            double d3 = (double)gameState3.getReputation() * 0.003 * gameState3.getPlayerSkills().getSilverTongueRepBonusMultiplier();
            double d4 = 1.0 + d3;
            double d5 = gameState3.getSubsidiaryCompanyIds().contains("rival_moocorp") ? 1.2 : 1.0;
            Object[] objectArray = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId(), ProductCatalog.INSTANCE.getCREAM().getId()};
            boolean bl7 = CollectionsKt.listOf((Object[])objectArray).contains(productType);
            double d6 = bl7 && gameState3.getSubsidiaryCompanyIds().contains("rival_lacto_dynasty") ? 1.35 : 1.0;
            double d7 = gameState3.getEndgamePriceMultiplier();
            List list2 = CollectionsKt.toMutableList((Collection)gameState3.getInventory());
            List list3 = gameState3.getInventoryMethod() == InventoryMethod.LIFO ? CollectionsKt.reversed((Iterable)list) : list;
            for (InventoryBatch inventoryBatch : list3) {
                if (n6 <= 0) break;
                int n7 = list2.indexOf(inventoryBatch);
                if (n7 == -1) continue;
                int n8 = RangesKt.coerceAtMost((int)n6, (int)inventoryBatch.getQuantity());
                double d8 = d2 * (0.8 + inventoryBatch.getQuality() * 0.2) * d4 * d5 * d6 * d7;
                double d9 = (double)n8 * d8;
                d += d9;
                n6 -= n8;
                if (inventoryBatch.getQuantity() <= n8) {
                    list2.remove(n7);
                    continue;
                }
                list2.set(n7, InventoryBatch.copy$default(inventoryBatch, null, null, null, inventoryBatch.getQuantity() - n8, 0.0, 0, 0, 0.0f, false, 503, null));
            }
            Integer n9 = gameState3.getTodaySoldUnits().get(productType);
            int n10 = n9 != null ? n9 : 0;
            Map map = MapsKt.plus(gameState3.getTodaySoldUnits(), (Pair)TuplesKt.to((Object)productType, (Object)(n10 + n)));
            String string = ProductCatalog.INSTANCE.getById(productType).getName();
            String string2 = "%.2f";
            Object[] objectArray2 = new Object[]{d};
            String string3 = String.format(string2, Arrays.copyOf(objectArray2, objectArray2.length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Sold " + n + " units of " + string + " for +$" + string3));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() + d, 0, 0, null, null, 0, 0, 0, null, null, null, list2, null, null, null, map, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -139269, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void sellInventoryBatch(@NotNull String batchId, int quantityToSell) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)batchId, (String)"batchId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            MarketItemState marketItemState;
            int n;
            GameState gameState3;
            block7: {
                int n2;
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                List<InventoryBatch> list = gameState3.getInventory();
                boolean bl3 = false;
                int n3 = 0;
                Iterator<InventoryBatch> iterator = list.iterator();
                while (iterator.hasNext()) {
                    InventoryBatch inventoryBatch;
                    InventoryBatch inventoryBatch2 = inventoryBatch = iterator.next();
                    boolean bl4 = false;
                    if (Intrinsics.areEqual((Object)inventoryBatch2.getBatchId(), (Object)batchId)) {
                        n2 = n3;
                        break block7;
                    }
                    ++n3;
                }
                n2 = n = -1;
            }
            if (n == -1) {
                gameState = gameState3;
                continue;
            }
            InventoryBatch inventoryBatch = gameState3.getInventory().get(n);
            boolean bl5 = Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId());
            if (inventoryBatch.isSpoiled() && !bl5) {
                this._snackBarMessage.setValue((Object)"Cannot sell spoiled batch as regular product! Please dump spoiled goods.");
                gameState = gameState3;
                continue;
            }
            int n4 = RangesKt.coerceIn((int)quantityToSell, (int)1, (int)inventoryBatch.getQuantity());
            MarketItemState marketItemState2 = marketItemState = gameState3.getMarketPrices().get(inventoryBatch.getItemId());
            double d = marketItemState2 != null ? marketItemState2.getCurrentPrice() : ProductCatalog.INSTANCE.getById(inventoryBatch.getItemId()).getBasePrice();
            double d2 = (double)gameState3.getReputation() * 0.003 * gameState3.getPlayerSkills().getSilverTongueRepBonusMultiplier();
            double d3 = 1.0 + d2;
            double d4 = gameState3.getSubsidiaryCompanyIds().contains("rival_moocorp") ? 1.2 : 1.0;
            Object[] objectArray = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId(), ProductCatalog.INSTANCE.getCREAM().getId()};
            boolean bl6 = CollectionsKt.listOf((Object[])objectArray).contains(inventoryBatch.getItemId());
            double d5 = bl6 && gameState3.getSubsidiaryCompanyIds().contains("rival_lacto_dynasty") ? 1.35 : 1.0;
            double d6 = gameState3.getEndgamePriceMultiplier();
            double d7 = d * (0.8 + inventoryBatch.getQuality() * 0.2) * d3 * d4 * d5 * d6;
            double d8 = (double)n4 * d7;
            List list = CollectionsKt.toMutableList((Collection)gameState3.getInventory());
            if (inventoryBatch.getQuantity() <= n4) {
                list.remove(n);
            } else {
                list.set(n, InventoryBatch.copy$default(inventoryBatch, null, null, null, inventoryBatch.getQuantity() - n4, 0.0, 0, 0, 0.0f, false, 503, null));
            }
            Integer n5 = gameState3.getTodaySoldUnits().get(inventoryBatch.getItemId());
            int n6 = n5 != null ? n5 : 0;
            Map map = MapsKt.plus(gameState3.getTodaySoldUnits(), (Pair)TuplesKt.to((Object)inventoryBatch.getItemId(), (Object)(n6 + n4)));
            String string = inventoryBatch.getItemName();
            String string2 = "%.2f";
            Object[] objectArray2 = new Object[]{d8};
            String string3 = String.format(string2, Arrays.copyOf(objectArray2, objectArray2.length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Sold " + n4 + " units of " + string + " for +$" + string3));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() + d8, 0, 0, null, null, 0, 0, 0, null, null, null, list, null, null, null, map, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -139269, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void sellAllOfProduct(@NotNull String productId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)productId, (String)"productId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterTo\10;
            Object object22;
            MarketItemState marketItemState;
            void $this$filterTo\4;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            boolean bl3 = Intrinsics.areEqual((Object)productId, (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId());
            Iterable iterable = gameState3.getInventory();
            boolean $i$f$filter\3\19412 = false;
            Iterable iterable2 = iterable;
            Collection collection = new ArrayList();
            boolean bl4 = false;
            for (Object t : $this$filterTo\4) {
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                boolean bl5 = false;
                if (!(Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)productId) && (bl3 || !inventoryBatch.isSpoiled()))) continue;
                collection.add(t);
            }
            List list = (List)collection;
            Iterable $i$f$filter\3\19412 = list;
            int n = 0;
            for (Object t : $i$f$filter\3\19412) {
                void it\6;
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                int n2 = n;
                boolean bl6 = false;
                int n3 = it\6.getQuantity();
                n = n2 + n3;
            }
            int n4 = n;
            if (n4 == 0) {
                this._snackBarMessage.setValue((Object)"No stock to sell for this product.");
                gameState = gameState3;
                continue;
            }
            MarketItemState marketItemState2 = marketItemState = gameState3.getMarketPrices().get(productId);
            double d = marketItemState2 != null ? marketItemState2.getCurrentPrice() : ProductCatalog.INSTANCE.getById(productId).getBasePrice();
            double d2 = (double)gameState3.getReputation() * 0.003 * gameState3.getPlayerSkills().getSilverTongueRepBonusMultiplier();
            double d3 = 1.0 + d2;
            double d4 = gameState3.getSubsidiaryCompanyIds().contains("rival_moocorp") ? 1.2 : 1.0;
            Object[] objectArray = new String[]{ProductCatalog.INSTANCE.getAGED_CHEDDAR().getId(), ProductCatalog.INSTANCE.getFRESH_CHEESE().getId(), ProductCatalog.INSTANCE.getBUTTER().getId(), ProductCatalog.INSTANCE.getCREAM().getId()};
            boolean bl7 = CollectionsKt.listOf((Object[])objectArray).contains(productId);
            double d5 = bl7 && gameState3.getSubsidiaryCompanyIds().contains("rival_lacto_dynasty") ? 1.35 : 1.0;
            double d6 = gameState3.getEndgamePriceMultiplier();
            double d7 = 0.0;
            Iterable iterable3 = list;
            boolean bl8 = false;
            for (Object object22 : iterable3) {
                InventoryBatch inventoryBatch = (InventoryBatch)object22;
                boolean bl9 = false;
                double d8 = d * (0.8 + inventoryBatch.getQuality() * 0.2) * d3 * d4 * d5 * d6;
                d7 += (double)inventoryBatch.getQuantity() * d8;
            }
            Iterable iterable4 = gameState3.getInventory();
            boolean bl10 = false;
            object22 = iterable4;
            Collection collection2 = new ArrayList();
            boolean bl11 = false;
            for (Object t : $this$filterTo\10) {
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                boolean bl12 = false;
                if (!(!Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)productId) || inventoryBatch.isSpoiled() && !bl3)) continue;
                collection2.add(t);
            }
            List list2 = (List)collection2;
            Integer n5 = gameState3.getTodaySoldUnits().get(productId);
            int n6 = n5 != null ? n5 : 0;
            Map map = MapsKt.plus(gameState3.getTodaySoldUnits(), (Pair)TuplesKt.to((Object)productId, (Object)(n6 + n4)));
            String string = ProductCatalog.INSTANCE.getById(productId).getName();
            String string2 = "%.2f";
            Object[] objectArray2 = new Object[]{d7};
            String string3 = String.format(string2, Arrays.copyOf(objectArray2, objectArray2.length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Liquidated " + n4 + " units of " + string + " for +$" + string3));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() + d7, 0, 0, null, null, 0, 0, 0, null, null, null, list2, null, null, null, map, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -139269, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void buyBuilding(@NotNull String buildingId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Object v0;
            GameState gameState3;
            block11: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getBuildings();
                boolean bl3 = false;
                for (Object t : iterable) {
                    Building building = (Building)t;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)building.getId(), (Object)buildingId)) continue;
                    v0 = t;
                    break block11;
                }
                v0 = null;
            }
            Building building = v0;
            if (building == null) {
                this._snackBarMessage.setValue((Object)"Facility blueprint not found.");
                gameState = gameState3;
                continue;
            }
            if (building.isConstructed()) {
                this._snackBarMessage.setValue((Object)(building.getName() + " is already constructed. Upgrade it instead!"));
                gameState = gameState3;
                continue;
            }
            if (gameState3.isBuildingUnderConstruction(buildingId)) {
                this._snackBarMessage.setValue((Object)(building.getName() + " is already under construction!"));
                gameState = gameState3;
                continue;
            }
            if (building.getRequiredTechId() != null && !gameState3.getUnlockedTechIds().contains(building.getRequiredTechId())) {
                TechTreeNode techTreeNode;
                Object object2;
                Object v2;
                block12: {
                    Iterable iterable = TechCatalog.INSTANCE.getALL_TECHS();
                    boolean bl5 = false;
                    for (Object t : iterable) {
                        TechTreeNode techTreeNode2 = (TechTreeNode)t;
                        boolean bl6 = false;
                        if (!Intrinsics.areEqual((Object)techTreeNode2.getId(), (Object)building.getRequiredTechId())) continue;
                        v2 = t;
                        break block12;
                    }
                    v2 = null;
                }
                if ((object2 = (techTreeNode = (TechTreeNode)v2)) == null || (object2 = ((TechTreeNode)object2).getName()) == null) {
                    object2 = "Prerequisite";
                }
                this._snackBarMessage.setValue((Object)("Locked! Requires R&D Technology: '" + (String)object2 + "'."));
                gameState = gameState3;
                continue;
            }
            if (gameState3.getUsedLand() + building.getLandRequired() > gameState3.getTotalLandCapacity()) {
                this._snackBarMessage.setValue((Object)("Insufficient Real Estate! Expand Land in Facilities tab (" + gameState3.getUsedLand() + "/" + gameState3.getTotalLandCapacity() + " plots used)."));
                gameState = gameState3;
                continue;
            }
            if (gameState3.getCash() < building.getBaseCost()) {
                String string = building.getName();
                double d = building.getBaseCost();
                String iterable = "%.2f";
                Object[] bl5 = new Object[]{gameState3.getCash()};
                String string2 = String.format(iterable, Arrays.copyOf(bl5, bl5.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Cannot afford " + string + ". Requires $" + d + " (Have $" + string2 + ")."));
                gameState = gameState3;
                continue;
            }
            ActiveProject activeProject = new ActiveProject(null, ProjectType.FACILITY_CONSTRUCTION, buildingId, building.getName(), building.getIconEmoji(), building.getDaysToComplete(), building.getDaysToComplete(), 0, 120.0, false, 641, null);
            ActiveProject activeProject2 = this.tryAssignProject(gameState3, activeProject);
            if (activeProject2 == null) {
                this._snackBarMessage.setValue((Object)"All Slots & Crews Busy! Upgrade Multi-Tasking or assign more Construction Crews.");
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("Started construction of " + building.getName() + "! (" + building.getDaysToComplete() + " days to finish)."));
            double d = gameState3.getCash() - building.getBaseCost();
            List list = CollectionsKt.plus((Collection)gameState3.getActiveProjects(), (Object)activeProject2);
            int n = RangesKt.coerceAtMost((int)(gameState3.getReputation() + 2), (int)100);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d, n, 0, null, null, 0, 0, 0, list, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -1037, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void upgradeBuilding(@NotNull String buildingId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Object v0;
            Object[] objectArray;
            GameState gameState3;
            block7: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getBuildings();
                boolean bl3 = false;
                for (Object object2 : iterable) {
                    objectArray = (Object[])object2;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)objectArray.getId(), (Object)buildingId)) continue;
                    v0 = object2;
                    break block7;
                }
                v0 = null;
            }
            Building building = v0;
            if (building == null || !building.isConstructed()) {
                this._snackBarMessage.setValue((Object)"Facility not built yet.");
                gameState = gameState3;
                continue;
            }
            if (building.getLevel() >= building.getMaxLevel()) {
                this._snackBarMessage.setValue((Object)(building.getName() + " is already at max level!"));
                gameState = gameState3;
                continue;
            }
            if (gameState3.isBuildingUnderUpgrade(buildingId)) {
                this._snackBarMessage.setValue((Object)(building.getName() + " upgrade is already in progress!"));
                gameState = gameState3;
                continue;
            }
            double d = building.getUpgradeCost();
            if (gameState3.getCash() < d) {
                Object object2;
                object2 = "%.2f";
                objectArray = new Object[]{d};
                String string = String.format(object2, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient cash. Upgrading requires $" + string + "."));
                gameState = gameState3;
                continue;
            }
            int n = building.getUpgradeDaysToComplete();
            int n2 = building.getLevel() + 1;
            ActiveProject activeProject = new ActiveProject(null, ProjectType.FACILITY_UPGRADE, buildingId, building.getName() + " (Lv." + n2 + ")", building.getIconEmoji(), n, n, n2, 150.0, false, 513, null);
            ActiveProject activeProject2 = this.tryAssignProject(gameState3, activeProject);
            if (activeProject2 == null) {
                this._snackBarMessage.setValue((Object)"All Slots & Crews Busy! Upgrade Multi-Tasking or assign more Construction Crews.");
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("Commissioned upgrade for " + building.getName() + " to Level " + n2 + "! (" + n + " days)."));
            double d2 = gameState3.getCash() - d;
            List list = CollectionsKt.plus((Collection)gameState3.getActiveProjects(), (Object)activeProject2);
            int n3 = RangesKt.coerceAtMost((int)(gameState3.getReputation() + 1), (int)100);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d2, n3, 0, null, null, 0, 0, 0, list, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -1037, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void buyLandExpansion() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            double d = gameState3.getNextLandCost();
            if (gameState3.getCash() < d) {
                String string = "%.2f";
                Object[] objectArray = new Object[]{d};
                String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                string = "%.2f";
                objectArray = new Object[]{gameState3.getCash()};
                String string3 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Cannot afford land plot. Requires $" + string2 + " (Have $" + string3 + ")."));
                gameState = gameState3;
                continue;
            }
            int n = gameState3.getPurchasedLandPlots() + 1;
            int n2 = gameState3.getMaxLandCapacity() + n;
            this._snackBarMessage.setValue((Object)("Acquired real estate plot! Estate expanded to " + n2 + " plots \ud83c\udfe1."));
            LifetimeStats lifetimeStats = LifetimeStats.copy$default(gameState3.getStats(), 0, 0.0, 0L, 0L, 0, 0, 0, 0.0, 0L, 0, gameState3.getStats().getTotalLandPlotsBought() + 1, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0x3FFFBFF, null);
            double d2 = gameState3.getCash() - d;
            int n3 = RangesKt.coerceAtMost((int)(gameState3.getReputation() + 4), (int)100);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d2, n3, 0, null, null, 0, 0, n, null, lifetimeStats, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -2573, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void rushProject(@NotNull String projectId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)projectId, (String)"projectId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterNotTo\11;
            Iterable iterable;
            Iterable iterable2;
            Object v0;
            Object[] objectArray;
            GameState gameState3;
            block12: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable3 = gameState3.getActiveProjects();
                for (Object object2 : iterable3) {
                    objectArray = (Object[])object2;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)objectArray.getId(), (Object)projectId)) continue;
                    v0 = object2;
                    break block12;
                }
                v0 = null;
            }
            ActiveProject activeProject = v0;
            if (activeProject == null) {
                this._snackBarMessage.setValue((Object)"Project not found or already finished.");
                gameState = gameState3;
                continue;
            }
            double d = activeProject.getTotalRushCost();
            if (gameState3.getCash() < d) {
                Object object2;
                object2 = "%.2f";
                objectArray = new Object[]{d};
                String string = String.format(object2, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string, (String)"format(...)");
                object2 = "%.2f";
                objectArray = new Object[]{gameState3.getCash()};
                String string2 = String.format(object2, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient cash to rush! Requires $" + string + " (Have $" + string2 + ")."));
                gameState = gameState3;
                continue;
            }
            List list = gameState3.getBuildings();
            Set set = gameState3.getUnlockedTechIds();
            int n = 0;
            switch (WhenMappings.$EnumSwitchMapping$2[activeProject.getType().ordinal()]) {
                case 1: {
                    Building building;
                    Collection collection;
                    Iterable iterable4;
                    Iterable iterable5 = list;
                    boolean bl4 = false;
                    iterable2 = iterable5;
                    Collection collection2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable5, (int)10));
                    boolean bl5 = false;
                    for (Object t : iterable4) {
                        Building building2 = (Building)t;
                        collection = collection2;
                        boolean bl6 = false;
                        collection.add(Intrinsics.areEqual((Object)building.getId(), (Object)activeProject.getTargetId()) ? Building.copy$default(building, null, null, null, null, 1, true, 0.0, 0.0, 0, 0, 0, null, null, true, null, null, 0, 0, 0, 0.0, 0, 0, 0, 8380367, null) : building);
                    }
                    list = (List)collection2;
                    n = 1;
                    break;
                }
                case 2: {
                    void $this$mapTo\8;
                    Building building;
                    Collection collection;
                    Iterable iterable6 = list;
                    boolean bl7 = false;
                    Iterable iterable4 = iterable6;
                    Collection collection3 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable6, (int)10));
                    boolean bl8 = false;
                    for (Object t : $this$mapTo\8) {
                        building = (Building)t;
                        collection = collection3;
                        boolean bl9 = false;
                        collection.add(Intrinsics.areEqual((Object)((Building)((Object)iterable)).getId(), (Object)activeProject.getTargetId()) ? Building.copy$default((Building)((Object)iterable), null, null, null, null, activeProject.getTargetLevel(), false, 0.0, 0.0, 0, 0, 0, null, null, false, null, null, 0, 0, 0, 0.0, 0, 0, 0, 0x7FFFEF, null) : iterable);
                    }
                    list = (List)collection3;
                    break;
                }
                case 3: {
                    set = SetsKt.plus(set, (Object)activeProject.getTargetId());
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            String string = activeProject.getTargetName();
            Object object3 = "%.2f";
            Object[] objectArray2 = new Object[]{d};
            String string3 = String.format((String)object3, Arrays.copyOf(objectArray2, objectArray2.length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("\u26a1 RUSHED! Completed " + string + " immediately for $" + string3 + "!"));
            object3 = gameState3.getStats();
            int n2 = gameState3.getStats().getTotalRushCount() + 1;
            int n3 = gameState3.getStats().getFacilitiesBuilt() + n;
            LifetimeStats lifetimeStats = LifetimeStats.copy$default((LifetimeStats)object3, 0, 0.0, 0L, 0L, 0, n3, 0, 0.0, 0L, n2, 0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0x3FFFDDF, null);
            double d2 = gameState3.getCash() - d;
            iterable2 = list;
            Set set2 = set;
            Iterable iterable7 = gameState3.getActiveProjects();
            boolean bl10 = false;
            iterable = iterable7;
            Collection collection = new ArrayList();
            boolean bl11 = false;
            for (Object t : $this$filterNotTo\11) {
                ActiveProject activeProject2 = (ActiveProject)t;
                boolean bl12 = false;
                if (Intrinsics.areEqual((Object)activeProject2.getId(), (Object)projectId)) continue;
                collection.add(t);
            }
            List list2 = (List)collection;
            int n4 = RangesKt.coerceAtMost((int)(gameState3.getReputation() + 2), (int)100);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d2, n4, 0, set2, null, 0, 0, 0, list2, lifetimeStats, null, null, (List)iterable2, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -19501, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void toggleBuildingOperational(@NotNull String buildingId) {
        Collection collection;
        List list;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$mapTo\4;
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
            Iterable iterable = gameState.getBuildings();
            boolean bl3 = false;
            Iterable iterable2 = iterable;
            collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
            boolean bl4 = false;
            for (Object t : $this$mapTo\4) {
                Building building;
                void b\5;
                Building building2 = (Building)t;
                Collection collection2 = collection;
                boolean bl5 = false;
                if (Intrinsics.areEqual((Object)b\5.getId(), (Object)buildingId) && b\5.isConstructed()) {
                    boolean bl6 = !b\5.isOperational();
                    String string = bl6 ? "Activated" : "Pausd / Idle";
                    this._snackBarMessage.setValue((Object)(string + " " + b\5.getName() + "."));
                    building = Building.copy$default((Building)b\5, null, null, null, null, 0, false, 0.0, 0.0, 0, 0, 0, null, null, bl6, null, null, 0, 0, 0, 0.0, 0, 0, 0, 0x7FDFFF, null);
                } else {
                    building = b\5;
                }
                collection2.add(building);
            }
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, list = (List)collection, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -16385, 262143, null))));
    }

    /*
     * WARNING - void declaration
     */
    public final void setFacilityRecipe(@NotNull String buildingId, @NotNull ProcessingRecipe recipe) {
        Collection collection;
        List list;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        Intrinsics.checkNotNullParameter((Object)recipe, (String)"recipe");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$mapTo\4;
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
            Iterable iterable = gameState.getBuildings();
            boolean bl3 = false;
            Iterable iterable2 = iterable;
            collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
            boolean bl4 = false;
            for (Object t : $this$mapTo\4) {
                Building building;
                void b\5;
                Building building2 = (Building)t;
                Collection collection2 = collection;
                boolean bl5 = false;
                if (Intrinsics.areEqual((Object)b\5.getId(), (Object)buildingId) && b\5.isConstructed()) {
                    this._snackBarMessage.setValue((Object)("Set " + b\5.getName() + " recipe to: " + recipe.getName()));
                    building = Building.copy$default((Building)b\5, null, null, null, null, 0, false, 0.0, 0.0, 0, 0, 0, recipe, null, false, null, null, 0, 0, 0, 0.0, 0, 0, 0, 0x7FF7FF, null);
                } else {
                    building = b\5;
                }
                collection2.add(building);
            }
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, list = (List)collection, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -16385, 262143, null))));
    }

    public final void borrowLoan(double amount) {
        GameState gameState;
        GameState gameState2;
        Object object;
        if (amount <= 0.0) {
            return;
        }
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            double d = gameState3.getBank().getMaxCreditLimit() - gameState3.getBank().getTotalDebt();
            if (amount > d) {
                String string = "%.2f";
                Object[] objectArray = new Object[]{d};
                String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Loan exceeds max available credit limit ($" + string2 + " max)."));
                gameState = gameState3;
                continue;
            }
            double d2 = gameState3.getBank().getTotalDebt() + amount;
            String string = "%.2f";
            Object[] objectArray = new Object[]{amount};
            String string3 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Approved loan of $" + string3 + ". Daily interest: " + gameState3.getBank().getDailyInterestRate() * (double)100 + "%."));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() + amount, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, BankState.copy$default(gameState3.getBank(), d2, gameState3.getBank().getDaysInDebt() == 0 ? 1 : gameState3.getBank().getDaysInDebt(), 0.0, 0.0, 0, 0.0, 60, null), null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -65541, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void repayLoan(double amount) {
        GameState gameState;
        GameState gameState2;
        Object object;
        if (amount <= 0.0) {
            return;
        }
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            if (!gameState3.getBank().isInDebt()) {
                this._snackBarMessage.setValue((Object)"You currently have no outstanding loan debt.");
                gameState = gameState3;
                continue;
            }
            double d = RangesKt.coerceAtMost((double)RangesKt.coerceAtMost((double)amount, (double)gameState3.getBank().getTotalDebt()), (double)gameState3.getCash());
            if (d <= 0.0) {
                this._snackBarMessage.setValue((Object)"Insufficient cash to repay loan.");
                gameState = gameState3;
                continue;
            }
            double d2 = gameState3.getBank().getTotalDebt() - d;
            String string = "%.2f";
            Object[] objectArray = new Object[]{d};
            String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("Repaid $" + string2 + " to the bank."));
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, gameState3.getCash() - d, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, BankState.copy$default(gameState3.getBank(), d2, d2 <= 0.0 ? 0 : gameState3.getBank().getDaysInDebt(), 0.0, 0.0, 0, 0.0, 60, null), null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -65541, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void constructOrUpgradeBuilding(@NotNull String buildingId) {
        Object v0;
        block4: {
            Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
            Iterable iterable = ((GameState)this._gameState.getValue()).getBuildings();
            boolean bl = false;
            for (Object t : iterable) {
                Building building = (Building)t;
                boolean bl2 = false;
                if (!Intrinsics.areEqual((Object)building.getId(), (Object)buildingId)) continue;
                v0 = t;
                break block4;
            }
            v0 = null;
        }
        Building building = v0;
        if (building == null) {
            return;
        }
        Building building2 = building;
        if (!building2.isConstructed()) {
            this.buyBuilding(buildingId);
        } else {
            this.upgradeBuilding(buildingId);
        }
    }

    public final void setBuildingRecipe(@NotNull String buildingId, int recipeIndex) {
        Object v0;
        block3: {
            Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
            Iterable iterable = ((GameState)this._gameState.getValue()).getBuildings();
            for (Object t : iterable) {
                Building building = (Building)t;
                boolean bl = false;
                if (!Intrinsics.areEqual((Object)building.getId(), (Object)buildingId)) continue;
                v0 = t;
                break block3;
            }
            v0 = null;
        }
        Building building = v0;
        if (building == null) {
            return;
        }
        Building building2 = building;
        boolean bl = 0 <= recipeIndex ? recipeIndex < ((Collection)building2.getAvailableRecipes()).size() : false;
        if (bl) {
            this.setFacilityRecipe(buildingId, building2.getAvailableRecipes().get(recipeIndex));
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void updateFacilityAllocation(@NotNull String buildingId, int percentage) {
        Collection<void> collection;
        List list;
        NewsEvent newsEvent;
        LifetimeStats lifetimeStats;
        List list2;
        int n;
        int n2;
        int n3;
        PlayerSkills playerSkills;
        Set set;
        int n4;
        int n5;
        double d;
        int n6;
        InventoryMethod inventoryMethod;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$mapTo\4;
            void $this$map\3;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            Iterable iterable = gameState3.getBuildings();
            list = null;
            newsEvent = null;
            lifetimeStats = null;
            list2 = null;
            n = 0;
            n2 = 0;
            n3 = 0;
            playerSkills = null;
            set = null;
            n4 = 0;
            n5 = 0;
            d = 0.0;
            n6 = 0;
            inventoryMethod = null;
            gameState = gameState3;
            boolean bl3 = false;
            void var26_25 = $this$map\3;
            Collection collection2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\3, (int)10));
            boolean bl4 = false;
            for (Object t : $this$mapTo\4) {
                void b\5;
                Building building = (Building)t;
                collection = collection2;
                boolean bl5 = false;
                collection.add((void)(Intrinsics.areEqual((Object)b\5.getId(), (Object)buildingId) ? Building.copy$default((Building)b\5, null, null, null, null, 0, false, 0.0, 0.0, 0, 0, 0, null, null, false, null, null, 0, 0, 0, 0.0, 0, RangesKt.coerceIn((int)percentage, (int)0, (int)100), 0, 0x5FFFFF, null) : b\5));
            }
            collection = (List)collection2;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, inventoryMethod, n6, d, n5, n4, set, playerSkills, n3, n2, n, list2, lifetimeStats, newsEvent, list, collection, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -16385, 262143, null))));
    }

    public final void setInventoryMethod(@NotNull InventoryMethod method) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)((Object)method), (String)"method");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, method, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -2, 262143, null))));
        this._snackBarMessage.setValue((Object)("Inventory processing method set to " + method.name()));
    }

    /*
     * WARNING - void declaration
     */
    public final void upgradeFactoryCapacity(@NotNull String buildingId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)buildingId, (String)"buildingId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Collection<Building> collection;
            void $this$mapTo\5;
            void $this$map\4;
            String string;
            Building building;
            Object v0;
            Object[] objectArray;
            Object object22;
            GameState gameState3;
            block12: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getBuildings();
                for (Object object22 : iterable) {
                    objectArray = (Object[])object22;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)objectArray.getId(), (Object)buildingId)) continue;
                    v0 = object22;
                    break block12;
                }
                v0 = null;
            }
            if ((Building)v0 == null) {
                gameState = gameState3;
                continue;
            }
            switch (building.getCapacityTier()) {
                case 1: {
                    String string2 = "tech_industrial_throughput_1";
                    break;
                }
                case 2: {
                    String string2 = "tech_industrial_throughput_2";
                    break;
                }
                case 3: {
                    String string2 = "tech_industrial_throughput_3";
                    break;
                }
                default: {
                    String string2 = string = null;
                }
            }
            if (string != null && !gameState3.getUnlockedTechIds().contains(string)) {
                this._snackBarMessage.setValue((Object)"Required technology not researched yet!");
                gameState = gameState3;
                continue;
            }
            if (building.getCapacityTier() >= 4) {
                this._snackBarMessage.setValue((Object)"Maximum capacity tier reached.");
                gameState = gameState3;
                continue;
            }
            double d = building.getCapacityUpgradeCost();
            if (gameState3.getCash() < d) {
                object22 = "%,.0f";
                objectArray = new Object[]{d};
                String string3 = String.format(object22, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient funds for capacity upgrade. Need $" + string3));
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("\u2705 " + building.getName() + " throughput capacity upgraded to Tier " + (building.getCapacityTier() + 1) + "!"));
            Building building2 = Building.copy$default(building, null, null, null, null, 0, false, 0.0, 0.0, 0, 0, 0, null, null, false, null, null, 0, 0, 0, 0.0, 0, 0, building.getCapacityTier() + 1, 0x3FFFFF, null);
            object22 = gameState3.getBuildings();
            List list = null;
            NewsEvent newsEvent = null;
            LifetimeStats lifetimeStats = null;
            List list2 = null;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            PlayerSkills playerSkills = null;
            Set set = null;
            int n4 = 0;
            int n5 = 0;
            double d2 = gameState3.getCash() - d;
            int n6 = 0;
            InventoryMethod inventoryMethod = null;
            GameState gameState4 = gameState3;
            boolean bl4 = false;
            void bl3 = $this$map\4;
            Collection collection2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map\4, (int)10));
            boolean bl5 = false;
            for (Object t : $this$mapTo\5) {
                void it\6;
                Building building3 = (Building)t;
                collection = collection2;
                boolean bl6 = false;
                collection.add((Building)(Intrinsics.areEqual((Object)it\6.getId(), (Object)buildingId) ? building2 : it\6));
            }
            collection = (List)collection2;
            gameState = GameState.copy$default(gameState4, inventoryMethod, n6, d2, n5, n4, set, playerSkills, n3, n2, n, list2, lifetimeStats, newsEvent, list, collection, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -16389, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void takeBankLoan(double amount) {
        this.borrowLoan(amount);
    }

    public final void repayBankLoan(double amount) {
        this.repayLoan(amount);
    }

    public final void buyShares(@NotNull String rivalId, int quantity) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)rivalId, (String)"rivalId");
        if (quantity <= 0) {
            return;
        }
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            boolean bl2;
            List list;
            List list2;
            List list3;
            Object[] objectArray;
            String string;
            Object[] objectArray2;
            Set set;
            RivalCompany rivalCompany;
            Object v0;
            GameState gameState3;
            block14: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl3 = false;
                Iterable iterable = gameState3.getRivalCompanies();
                for (Object t : iterable) {
                    RivalCompany rivalCompany2 = (RivalCompany)t;
                    boolean bl4 = false;
                    if (!Intrinsics.areEqual((Object)rivalCompany2.getId(), (Object)rivalId)) continue;
                    v0 = t;
                    break block14;
                }
                v0 = null;
            }
            if ((rivalCompany = (RivalCompany)v0) == null) {
                rivalCompany = RivalCatalog.INSTANCE.getRivalById(rivalId);
            }
            RivalCompany rivalCompany3 = rivalCompany;
            int n = gameState3.getSharesOwned(rivalId);
            int n2 = RangesKt.coerceAtLeast((int)(rivalCompany3.getTotalShares() - n), (int)0);
            if (n2 <= 0) {
                this._snackBarMessage.setValue((Object)("You already own 100% of all circulating shares in " + rivalCompany3.getName() + "!"));
                gameState = gameState3;
                continue;
            }
            int n3 = RangesKt.coerceAtMost((int)quantity, (int)n2);
            double d = (double)n3 * rivalCompany3.getStockPrice();
            if (gameState3.getCash() < d) {
                String bl4 = "%.2f";
                Object[] objectArray3 = new Object[]{d};
                String string2 = String.format(bl4, Arrays.copyOf(objectArray3, objectArray3.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                bl4 = "%.2f";
                objectArray3 = new Object[]{gameState3.getCash()};
                String string3 = String.format(bl4, Arrays.copyOf(objectArray3, objectArray3.length));
                Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient liquid cash. Buying " + n3 + " shares requires $" + string2 + " (Have $" + string3 + ")."));
                gameState = gameState3;
                continue;
            }
            int n4 = n + n3;
            Map map = MapsKt.plus(gameState3.getRivalSharesOwned(), (Pair)TuplesKt.to((Object)rivalId, (Object)n4));
            boolean bl5 = gameState3.getSubsidiaryCompanyIds().contains(rivalId);
            boolean bl6 = n4 >= 51;
            Set set2 = set = bl6 && !bl5 ? SetsKt.plus(gameState3.getSubsidiaryCompanyIds(), (Object)rivalId) : gameState3.getSubsidiaryCompanyIds();
            if (bl6 && !bl5) {
                Iterable iterable = gameState3.getPendingContractOffers();
                boolean bl7 = false;
                Iterable iterable2 = iterable;
                objectArray2 = (Object[])new ArrayList();
                boolean bl8 = false;
                objectArray = string.iterator();
                while (objectArray.hasNext()) {
                    Object t = objectArray.next();
                    ContractOffer contractOffer = (ContractOffer)t;
                    boolean bl9 = false;
                    if (!(!Intrinsics.areEqual((Object)contractOffer.getRivalId(), (Object)rivalId))) continue;
                    objectArray2.add(t);
                }
                list3 = (List)objectArray2;
            } else {
                list3 = list2 = gameState3.getPendingContractOffers();
            }
            if (bl6 && !bl5) {
                list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83d\udea8 HOSTILE TAKEOVER COMPLETE: Acquired " + n4 + "% controlling stake in " + rivalCompany3.getName() + "! Subsidiary Perk Unlocked: " + rivalCompany3.getSubsidiaryPerkTitle())), (Iterable)gameState3.getDailyLogs());
            } else {
                String string4 = rivalCompany3.getName();
                String string5 = rivalCompany3.getTickerSymbol();
                string = "%.2f";
                objectArray2 = new Object[]{rivalCompany3.getStockPrice()};
                String string6 = String.format(string, Arrays.copyOf(objectArray2, objectArray2.length));
                Intrinsics.checkNotNullExpressionValue((Object)string6, (String)"format(...)");
                list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83d\udcc8 Stock Exchange: Acquired " + n3 + " shares of " + string4 + " (" + string5 + ") at $" + string6 + "/share.")), (Iterable)gameState3.getDailyLogs());
            }
            List list4 = list;
            boolean bl10 = set.size() >= RivalCatalog.INSTANCE.getALL_RIVALS().size();
            boolean bl11 = bl2 = bl10 && !gameState3.isEndgameCompleted() && !gameState3.isEndgameTriggered();
            if (bl2) {
                this._showEndgameDialog.setValue((Object)true);
            }
            if (bl6 && !bl5) {
                this._snackBarMessage.setValue((Object)("\ud83c\udfe2 HOSTILE TAKEOVER! " + rivalCompany3.getName() + " is now your subsidiary! Perk: " + rivalCompany3.getSubsidiaryPerkTitle()));
            } else {
                String string7 = rivalCompany3.getTickerSymbol();
                String string8 = "%.2f";
                objectArray = new Object[]{d};
                String string9 = String.format(string8, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string9, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("\ud83d\udcc8 Purchased " + n3 + " shares of " + string7 + " for $" + string9 + " (" + n4 + "% owned)"));
            }
            double d2 = gameState3.getCash() - d;
            List list5 = CollectionsKt.take((Iterable)list4, (int)25);
            boolean bl12 = gameState3.isEndgameTriggered() || bl2;
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d2, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list5, null, false, null, 0, 0, null, null, list2, null, 0, 0.0, map, set, bl12, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, 0x3BFBFFFB, 262142, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    public final void sellShares(@NotNull String rivalId, int quantity) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)rivalId, (String)"rivalId");
        if (quantity <= 0) {
            return;
        }
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            Object[] objectArray;
            String string;
            List list;
            Set set;
            int n;
            RivalCompany rivalCompany;
            RivalCompany rivalCompany2;
            Object v0;
            GameState gameState3;
            block7: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getRivalCompanies();
                for (Object t : iterable) {
                    RivalCompany rivalCompany3 = (RivalCompany)t;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)rivalCompany3.getId(), (Object)rivalId)) continue;
                    v0 = t;
                    break block7;
                }
                v0 = null;
            }
            if ((rivalCompany2 = (RivalCompany)v0) == null) {
                rivalCompany2 = rivalCompany = RivalCatalog.INSTANCE.getRivalById(rivalId);
            }
            if ((n = gameState3.getSharesOwned(rivalId)) <= 0) {
                this._snackBarMessage.setValue((Object)("You do not own any equity shares in " + rivalCompany.getName() + "."));
                gameState = gameState3;
                continue;
            }
            int n2 = RangesKt.coerceAtMost((int)quantity, (int)n);
            double d = (double)n2 * rivalCompany.getStockPrice();
            int n3 = n - n2;
            Map map = MapsKt.plus(gameState3.getRivalSharesOwned(), (Pair)TuplesKt.to((Object)rivalId, (Object)n3));
            boolean bl4 = gameState3.getSubsidiaryCompanyIds().contains(rivalId);
            boolean bl5 = n3 >= 51;
            Set set2 = set = bl4 && !bl5 ? SetsKt.minus(gameState3.getSubsidiaryCompanyIds(), (Object)rivalId) : gameState3.getSubsidiaryCompanyIds();
            if (bl4 && !bl5) {
                list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83d\udcc9 Divestment: Sold majority control in " + rivalCompany.getName() + ". Lost subsidiary privileges.")), (Iterable)gameState3.getDailyLogs());
            } else {
                String string2 = rivalCompany.getName();
                String string3 = rivalCompany.getTickerSymbol();
                string = "%.2f";
                objectArray = new Object[]{d};
                String string4 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string4, (String)"format(...)");
                list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83d\udcc9 Stock Exchange: Liquidated " + n2 + " shares of " + string2 + " (" + string3 + ") for +$" + string4 + ".")), (Iterable)gameState3.getDailyLogs());
            }
            List list2 = list;
            String string5 = rivalCompany.getTickerSymbol();
            string = "%.2f";
            objectArray = new Object[]{d};
            String string6 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string6, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("\ud83d\udcc9 Liquidated " + n2 + " shares of " + string5 + " for +$" + string6 + " (" + n3 + "% remaining)"));
            double d2 = gameState3.getCash() + d;
            List list3 = CollectionsKt.take((Iterable)list2, (int)25);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d2, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list3, null, false, null, 0, 0, null, null, null, null, 0, 0.0, map, set, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, 0x3FFBFFFB, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void launchCorporateSabotage(@NotNull String rivalId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)rivalId, (String)"rivalId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$mapTo\5;
            int n;
            double d;
            RivalCompany rivalCompany;
            Object v0;
            Object[] objectArray;
            GameState gameState3;
            block7: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getRivalCompanies();
                for (Object object2 : iterable) {
                    objectArray = (Object[])object2;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)objectArray.getId(), (Object)rivalId)) continue;
                    v0 = object2;
                    break block7;
                }
                v0 = null;
            }
            if ((RivalCompany)v0 == null) {
                gameState = gameState3;
                continue;
            }
            int n2 = 100;
            double d2 = 5000.0;
            if (gameState3.getResearchPoints() < n2 || gameState3.getCash() < d2) {
                Object object2;
                object2 = "%.2f";
                objectArray = new Object[]{d2};
                String string = String.format(object2, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Insufficient resources! Requires " + n2 + " RP & $" + string + " cash."));
                gameState = gameState3;
                continue;
            }
            int n3 = gameState3.getPlayerOffenseRating() + Random.Default.nextInt(1, 20);
            int n4 = rivalCompany.getDefenseRating() + Random.Default.nextInt(1, 20);
            RivalCompany rivalCompany2 = null;
            int n5 = 0;
            if (n3 > n4) {
                double d3 = rivalCompany.getNetWorth() * 0.15;
                d = RangesKt.coerceAtLeast((double)(rivalCompany.getNetWorth() - d3), (double)0.0);
                n = rivalCompany.getHostilityToPlayer() + 10;
                rivalCompany2 = RivalCompany.copy$default(rivalCompany, null, null, null, null, null, 0.0f, 0.0, 0.0, null, null, null, 0, 0.0, null, null, false, 0, d, 0, 0, null, n, 1, 0x1DFFFF, null);
                this._snackBarMessage.setValue((Object)("\u2705 CYBER OFFENSE SUCCESS! " + rivalCompany.getName() + " suffered massive financial damage and B2B lockout."));
            } else {
                n5 = 15;
                rivalCompany2 = RivalCompany.copy$default(rivalCompany, null, null, null, null, null, 0.0f, 0.0, 0.0, null, null, null, 0, 0.0, null, null, false, 0, 0.0, 0, 0, null, rivalCompany.getHostilityToPlayer() + 25, 0, 0x5FFFFF, null);
                this._snackBarMessage.setValue((Object)"\u274c SABOTAGE FAILED! Traced back to your IP. Reputation tanked.");
            }
            Iterable iterable = gameState3.getRivalCompanies();
            boolean bl4 = false;
            Iterable iterable2 = iterable;
            Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
            boolean bl5 = false;
            for (Object t : $this$mapTo\5) {
                void it\6;
                RivalCompany rivalCompany3 = (RivalCompany)t;
                Collection collection2 = collection;
                boolean bl6 = false;
                collection2.add(Intrinsics.areEqual((Object)it\6.getId(), (Object)rivalId) ? rivalCompany2 : it\6);
            }
            List list = (List)collection;
            d = gameState3.getCash() - d2;
            n = gameState3.getResearchPoints() - n2;
            int n6 = RangesKt.coerceAtLeast((int)(gameState3.getReputation() - n5), (int)0);
            GameState gameState4 = GameState.copy$default(gameState3, null, 0, d, n6, n, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, list, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -33554461, 262143, null);
            gameState = this.checkAchievements(gameState4);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void launchSmearCampaign(@NotNull String rivalId) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)rivalId, (String)"rivalId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            RivalCompany rivalCompany;
            Object v0;
            Object[] objectArray;
            GameState gameState3;
            block7: {
                object = mutableStateFlow.getValue();
                gameState3 = (GameState)object;
                boolean bl2 = false;
                Iterable iterable = gameState3.getRivalCompanies();
                for (Object object2 : iterable) {
                    objectArray = (Object[])object2;
                    boolean bl3 = false;
                    if (!Intrinsics.areEqual((Object)objectArray.getId(), (Object)rivalId)) continue;
                    v0 = object2;
                    break block7;
                }
                v0 = null;
            }
            if ((RivalCompany)v0 == null) {
                gameState = gameState3;
                continue;
            }
            if (gameState3.getSubsidiaryCompanyIds().contains(rivalId)) {
                this._snackBarMessage.setValue((Object)"Cannot execute smear campaigns on your own subsidiary!");
                gameState = gameState3;
                continue;
            }
            int n = 2;
            double d = 250.0;
            if (gameState3.getResearchPoints() < n || gameState3.getCash() < d) {
                Object object2;
                object2 = "%.2f";
                objectArray = new Object[]{d};
                String string = String.format(object2, Arrays.copyOf(objectArray, objectArray.length));
                Intrinsics.checkNotNullExpressionValue((Object)string, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("Smear campaign requires " + n + " RP and $" + string + " cash."));
                gameState = gameState3;
                continue;
            }
            int n2 = gameState3.getPlayerSkills().getSilverTongueLevel();
            int n3 = RangesKt.coerceAtMost((int)(50 + n2 * 15), (int)95);
            int n4 = Random.Default.nextInt(1, 101);
            boolean bl4 = n4 <= n3;
            double d2 = gameState3.getCash() - d;
            int n5 = gameState3.getResearchPoints() - n;
            if (bl4) {
                void $this$mapTo\5;
                double d3 = (double)((int)(rivalCompany.getStockPrice() * 0.6 * 100.0)) / 100.0;
                RivalCompany rivalCompany2 = RivalCompany.copy$default(rivalCompany, null, null, null, null, null, 0.0f, RangesKt.coerceAtLeast((double)d3, (double)10.0), -40.0, null, null, null, 0, 0.0, null, null, true, 2, 0.0, 0, 0, null, 0, 0, 8290111, null);
                Iterable iterable = gameState3.getRivalCompanies();
                boolean bl5 = false;
                Object object3 = iterable;
                Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
                boolean bl6 = false;
                for (Object t : $this$mapTo\5) {
                    void it\6;
                    RivalCompany rivalCompany3 = (RivalCompany)t;
                    Collection collection2 = collection;
                    boolean bl7 = false;
                    collection2.add(Intrinsics.areEqual((Object)it\6.getId(), (Object)rivalId) ? rivalCompany2 : it\6);
                }
                List list = (List)collection;
                String string = rivalCompany.getName();
                object3 = "%.2f";
                Object[] objectArray2 = new Object[]{d3};
                String string2 = String.format((String)object3, Arrays.copyOf(objectArray2, objectArray2.length));
                Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
                List list2 = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\ud83d\udcc9 Media Scandal: Leaked damaging intelligence on " + string + ", crashing stock by -40% to $" + string2 + ".")), (Iterable)gameState3.getDailyLogs());
                String string3 = rivalCompany.getTickerSymbol();
                object3 = "%.2f";
                objectArray2 = new Object[]{d3};
                String string4 = String.format((String)object3, Arrays.copyOf(objectArray2, objectArray2.length));
                Intrinsics.checkNotNullExpressionValue((Object)string4, (String)"format(...)");
                this._snackBarMessage.setValue((Object)("\ud83c\udfaf Smear Success (" + n3 + "% roll)! " + string3 + " shares plummeted -40% to $" + string4 + "!"));
                List list3 = CollectionsKt.take((Iterable)list2, (int)25);
                gameState = GameState.copy$default(gameState3, null, 0, d2, 0, n5, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list3, null, false, null, 0, 0, null, list, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -33816597, 262143, null);
                continue;
            }
            int n6 = RangesKt.coerceAtLeast((int)(gameState3.getReputation() - 5), (int)0);
            List list = CollectionsKt.plus((Collection)CollectionsKt.listOf((Object)("\u26a0\ufe0f PR Disaster: Defamation lawsuit from " + rivalCompany.getName() + " exposed smear attempt! Lost -5 Reputation.")), (Iterable)gameState3.getDailyLogs());
            this._snackBarMessage.setValue((Object)("\u26a0\ufe0f Smear Botched (" + n3 + "% chance)! " + rivalCompany.getName() + " countersued (-5 Rep)!"));
            List list4 = CollectionsKt.take((Iterable)list, (int)25);
            gameState = GameState.copy$default(gameState3, null, 0, d2, n6, n5, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list4, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -262173, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean checkWinState() {
        GameState gameState;
        GameState gameState2;
        Object object;
        boolean bl;
        GameState current = (GameState)this._gameState.getValue();
        if (((Collection)RivalCatalog.INSTANCE.getALL_RIVALS()).isEmpty()) return false;
        boolean bl2 = true;
        if (!bl2) return false;
        Iterable iterable = RivalCatalog.INSTANCE.getALL_RIVALS();
        boolean bl3 = false;
        if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) {
            bl = true;
        } else {
            for (Object t : iterable) {
                RivalCompany rivalCompany = (RivalCompany)t;
                boolean bl4 = false;
                if (current.getSharesOwned(rivalCompany.getId()) < 51) {
                    if (!current.getSubsidiaryCompanyIds().contains(rivalCompany.getId())) return false;
                }
                boolean bl5 = true;
                if (bl5) continue;
                return false;
            }
            bl = true;
        }
        if (!bl) return false;
        boolean bl6 = true;
        boolean allRivalsConquered = bl6;
        if (!allRivalsConquered) return allRivalsConquered;
        if (current.isEndgameCompleted()) return allRivalsConquered;
        if (current.isEndgameTriggered()) return allRivalsConquered;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl7 = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl8 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, true, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262142, null))));
        this._showEndgameDialog.setValue((Object)true);
        return true;
    }

    public final boolean saveGame() {
        boolean success = this.saveGameManager.saveGame((GameState)this._gameState.getValue());
        if (success) {
            this._hasSavedGame.setValue((Object)true);
            this._saveSummary.setValue((Object)SaveGameManager.getSaveSummary$default(this.saveGameManager, null, 1, null));
            this._snackBarMessage.setValue((Object)"\ud83d\udcbe Game saved successfully!");
        } else {
            this._snackBarMessage.setValue((Object)"\u26a0\ufe0f Failed to save game state.");
        }
        return success;
    }

    public final boolean loadGame(@Nullable String saveId) {
        boolean bl;
        GameState loadedState = this.saveGameManager.loadGame(saveId);
        if (loadedState != null) {
            this._gameState.setValue((Object)loadedState);
            this._showDailyReportDialog.setValue((Object)false);
            this._showNewsChronicleDialog.setValue((Object)false);
            this._showEndgameDialog.setValue((Object)(loadedState.isEndgameTriggered() && !loadedState.isEndgameCompleted() ? 1 : 0));
            this._hasSavedGame.setValue((Object)true);
            this._saveSummary.setValue((Object)this.saveGameManager.getSaveSummary(loadedState.getSaveId()));
            int n = loadedState.getDay();
            String string = "%.2f";
            Object[] objectArray = new Object[]{loadedState.getCash()};
            String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"format(...)");
            this._snackBarMessage.setValue((Object)("\ud83d\udcc2 Game loaded: Day " + n + " ($" + string2 + ")"));
            this.checkWinState();
            bl = true;
        } else {
            this._snackBarMessage.setValue((Object)"\u26a0\ufe0f No valid save file found.");
            bl = false;
        }
        return bl;
    }

    public static /* synthetic */ boolean loadGame$default(GameViewModel gameViewModel, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = null;
        }
        return gameViewModel.loadGame(string);
    }

    public final void startNewGame() {
        GameState newState = new GameState(null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -1, 262143, null);
        this._gameState.setValue((Object)newState);
        this._showDailyReportDialog.setValue((Object)false);
        this._showNewsChronicleDialog.setValue((Object)false);
        this._showEndgameDialog.setValue((Object)false);
        this.saveGameManager.saveGame(newState);
        this._hasSavedGame.setValue((Object)true);
        this._saveSummary.setValue((Object)this.saveGameManager.getSaveSummary(newState.getSaveId()));
        this._snackBarMessage.setValue((Object)"\ud83c\udf31 New Dairy Empire started!");
    }

    public final void deleteSave(@NotNull String saveId) {
        Intrinsics.checkNotNullParameter((Object)saveId, (String)"saveId");
        this.saveGameManager.clearSaveGame(saveId);
        if (Intrinsics.areEqual((Object)((GameState)this._gameState.getValue()).getSaveId(), (Object)saveId)) {
            this.startNewGame();
        }
    }

    public final void chooseEndgameOption(@NotNull EndgameChoice choice) {
        GameState gameState;
        GameState gameState2;
        GameState gameState3;
        Object object;
        Intrinsics.checkNotNullParameter((Object)((Object)choice), (String)"choice");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState4 = (GameState)object;
            boolean bl2 = false;
            this._showEndgameDialog.setValue((Object)false);
            List list = new ArrayList();
            switch (WhenMappings.$EnumSwitchMapping$3[choice.ordinal()]) {
                case 1: {
                    list.add("\ud83c\udf3e THE GOLDEN PITCHFORK: You chose agrarian benevolence. All consumer prices slashed by 80% to feed the world.");
                    this._snackBarMessage.setValue((Object)"\ud83c\udf3e The Golden Pitchfork: Dairy is now subsidized for all humanity!");
                    double d = choice.getPriceMultiplier();
                    List list2 = CollectionsKt.take((Iterable)CollectionsKt.plus((Collection)list, (Iterable)gameState4.getDailyLogs()), (int)25);
                    gameState = GameState.copy$default(gameState4, null, 0, 0.0, 100, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list2, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, true, choice, d, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -262153, 262128, null);
                    break;
                }
                case 2: {
                    list.add("\ud83d\udc8e THE DIAMOND COWBELL: You chose unbridled monopoly capitalism! All product prices increased by +300%.");
                    this._snackBarMessage.setValue((Object)"\ud83d\udc8e The Diamond Cowbell: Total corporate monopoly achieved (+300% prices)!");
                    double d = choice.getPriceMultiplier();
                    List list2 = CollectionsKt.take((Iterable)CollectionsKt.plus((Collection)list, (Iterable)gameState4.getDailyLogs()), (int)25);
                    gameState = GameState.copy$default(gameState4, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, list2, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, true, choice, d, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -262145, 262128, null);
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState3 = this.checkAchievements(gameState2 = gameState))));
        this.saveGame();
    }

    public final void markFeatureAsSeen(@NotNull DrawerDestination destination) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)((Object)destination), (String)"destination");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            switch (WhenMappings.$EnumSwitchMapping$4[destination.ordinal()]) {
                case 1: {
                    if (gameState3.getUnlockedFeatures().isBoardroomNew()) {
                        gameState = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, UnlockedFeatures.copy$default(gameState3.getUnlockedFeatures(), false, false, false, false, false, false, 55, null), null, null, null, null, null, false, null, null, null, -1, 261887, null);
                        break;
                    }
                    gameState = gameState3;
                    break;
                }
                case 2: {
                    if (gameState3.getUnlockedFeatures().isStockMarketNew()) {
                        gameState = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, UnlockedFeatures.copy$default(gameState3.getUnlockedFeatures(), false, false, false, false, false, false, 47, null), null, null, null, null, null, false, null, null, null, -1, 261887, null);
                        break;
                    }
                    gameState = gameState3;
                    break;
                }
                default: {
                    gameState = gameState3;
                }
            }
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
    }

    /*
     * WARNING - void declaration
     */
    public final void discardSpoiledGoods() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            void $this$filterTo\8;
            void $this$filter\7;
            void n;
            object = mutableStateFlow.getValue();
            GameState gameState3 = (GameState)object;
            boolean bl2 = false;
            Iterable iterable = gameState3.getInventory();
            boolean $i$f$filter\3\27332 = false;
            Iterable iterable2 = iterable;
            Object object2 = new ArrayList();
            boolean bl32 = false;
            for (Object t : n) {
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                boolean bl4 = false;
                if (!(inventoryBatch.isSpoiled() || Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId()))) continue;
                object2.add(t);
            }
            List list = (List)object2;
            Iterable $i$f$filter\3\27332 = list;
            int n = 0;
            for (Object bl32 : $i$f$filter\3\27332) {
                void it\6;
                InventoryBatch inventoryBatch = (InventoryBatch)bl32;
                int n2 = n;
                boolean bl5 = false;
                int n3 = it\6.getQuantity();
                n = n2 + n3;
            }
            int n4 = n;
            if (n4 == 0) {
                this._snackBarMessage.setValue((Object)"No spoiled goods found in the warehouse.");
                gameState = gameState3;
                continue;
            }
            this._snackBarMessage.setValue((Object)("Dumped " + n4 + " units of spoiled goods to clear warehouse space."));
            $i$f$filter\3\27332 = gameState3.getInventory();
            NewsEvent newsEvent = null;
            LifetimeStats lifetimeStats = null;
            List list2 = null;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            PlayerSkills playerSkills = null;
            Set set = null;
            int n8 = 0;
            int n9 = 0;
            double d = 0.0;
            int n10 = 0;
            InventoryMethod inventoryMethod = null;
            GameState gameState4 = gameState3;
            boolean bl6 = false;
            object2 = $this$filter\7;
            Collection collection = new ArrayList();
            boolean bl7 = false;
            for (Object t : $this$filterTo\8) {
                InventoryBatch inventoryBatch = (InventoryBatch)t;
                boolean bl8 = false;
                if (!(!inventoryBatch.isSpoiled() && !Intrinsics.areEqual((Object)inventoryBatch.getItemId(), (Object)ProductCatalog.INSTANCE.getSPOILED_MILK().getId()))) continue;
                collection.add(t);
            }
            List list3 = (List)collection;
            gameState = GameState.copy$default(gameState4, inventoryMethod, n10, d, n9, n8, set, playerSkills, n7, n6, n5, list2, lifetimeStats, newsEvent, list3, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, null, -8193, 262143, null);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = gameState)));
        this.saveGame();
    }

    public final void restartGame() {
        this.startNewGame();
    }

    public final void goCorporate() {
        GameState gameState;
        GameState gameState2;
        Object object;
        GameState gameState3 = (GameState)this._gameState.getValue();
        String string = UUID.randomUUID().toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, (String)"toString(...)");
        GameState backupState = GameState.copy$default(gameState3, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, string, "Farm_Backup", GamePhase.SANDBOX, false, null, null, null, -1, 247807, null);
        this.saveGameManager.saveGame(backupState);
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, "Corp_" + gameState.getSaveName(), GamePhase.CORPORATE, false, null, null, null, -1, 249855, null))));
        this._showMilestoneScreen.setValue((Object)false);
        this.saveGame();
    }

    public final void startSandbox() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, GamePhase.SANDBOX, false, null, null, null, -1, 253951, null))));
        this._showMilestoneScreen.setValue((Object)false);
        this.saveGame();
    }

    public final void retireSave() {
        GameState gameState;
        GameState gameState2;
        Object object;
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, GamePhase.COMPLETED, false, null, null, null, -1, 253951, null))));
        this._showMilestoneScreen.setValue((Object)false);
        this.saveGame();
    }

    public final void updateColdStoragePriority(@NotNull ColdStoragePriority priority) {
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)((Object)priority), (String)"priority");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, priority, null, null, -1, 229375, null))));
        this.saveGame();
    }

    public final void updateManualColdStorageAllocation(@NotNull String itemId, int allocation) {
        Map map;
        GameState gameState;
        GameState gameState2;
        Object object;
        Intrinsics.checkNotNullParameter((Object)itemId, (String)"itemId");
        MutableStateFlow<GameState> mutableStateFlow = this._gameState;
        boolean bl = false;
        do {
            object = mutableStateFlow.getValue();
            gameState = (GameState)object;
            boolean bl2 = false;
            map = MapsKt.toMutableMap(gameState.getManualColdStorageAllocations());
            if (allocation <= 0) {
                map.remove(itemId);
                continue;
            }
            map.put(itemId, allocation);
        } while (!mutableStateFlow.compareAndSet(object, (Object)(gameState2 = GameState.copy$default(gameState, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, map, null, -1, 196607, null))));
        this.saveGame();
    }

    private final GameState applyResearchEffect(GameState state, String techId) {
        Object v0;
        block2: {
            Iterable iterable = ResearchCatalog.INSTANCE.getALL_NODES();
            boolean bl = false;
            for (Object t : iterable) {
                ResearchNode researchNode = (ResearchNode)t;
                boolean bl2 = false;
                if (!Intrinsics.areEqual((Object)researchNode.getId(), (Object)techId)) continue;
                v0 = t;
                break block2;
            }
            v0 = null;
        }
        ResearchNode researchNode = v0;
        if (researchNode == null) {
            return state;
        }
        ResearchNode node = researchNode;
        Map newStatuses = MapsKt.toMutableMap(state.getResearchNodeStatuses());
        newStatuses.put(techId, NodeStatus.COMPLETED);
        GameState nextState = GameState.copy$default(state, null, 0, 0.0, 0, 0, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, false, null, 0, 0, null, null, null, null, 0, 0.0, null, null, false, false, null, 0.0, null, null, null, false, null, null, null, null, null, null, false, null, null, newStatuses, -1, 131071, null);
        return nextState;
    }

    public static final /* synthetic */ SettingsRepository access$getSettingsRepository$p(GameViewModel $this) {
        return $this.settingsRepository;
    }

    public static final /* synthetic */ MutableStateFlow access$get_gameState$p(GameViewModel $this) {
        return $this._gameState;
    }

    public static final /* synthetic */ MutableStateFlow access$get_showMilestoneScreen$p(GameViewModel $this) {
        return $this._showMilestoneScreen;
    }

    public static final /* synthetic */ MutableStateFlow access$get_showNewsChronicleDialog$p(GameViewModel $this) {
        return $this._showNewsChronicleDialog;
    }

    public static final /* synthetic */ MutableStateFlow access$get_showDailyReportDialog$p(GameViewModel $this) {
        return $this._showDailyReportDialog;
    }

    public static final /* synthetic */ int access$calculateDailyUsage(GameViewModel $this, GameState state, String itemId) {
        return $this.calculateDailyUsage(state, itemId);
    }

    public static final /* synthetic */ MutableStateFlow access$get_showEndgameDialog$p(GameViewModel $this) {
        return $this._showEndgameDialog;
    }

    public static final /* synthetic */ GameState access$applyResearchEffect(GameViewModel $this, GameState state, String techId) {
        return $this.applyResearchEffect(state, techId);
    }

    public static final /* synthetic */ MutableStateFlow access$get_snackBarMessage$p(GameViewModel $this) {
        return $this._snackBarMessage;
    }

    @Metadata(mv={2, 2, 0}, k=3, xi=48)
    public static final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;
        public static final /* synthetic */ int[] $EnumSwitchMapping$2;
        public static final /* synthetic */ int[] $EnumSwitchMapping$3;
        public static final /* synthetic */ int[] $EnumSwitchMapping$4;

        static {
            int[] nArray = new int[NetWorthPhase.values().length];
            try {
                nArray[NetWorthPhase.STARTUP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[NetWorthPhase.TENSION.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[NetWorthPhase.CORPORATE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            $EnumSwitchMapping$0 = nArray;
            nArray = new int[SkillType.values().length];
            try {
                nArray[SkillType.STAMINA.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.HUSTLER.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.EFFICIENCY_EXPERT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.SILVER_TONGUE.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.BOVINE_GENETICS.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.COLD_CHAIN_LOGISTICS.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[SkillType.MULTI_TASKING.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            $EnumSwitchMapping$1 = nArray;
            nArray = new int[ProjectType.values().length];
            try {
                nArray[ProjectType.FACILITY_CONSTRUCTION.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[ProjectType.FACILITY_UPGRADE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[ProjectType.TECH_RESEARCH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            $EnumSwitchMapping$2 = nArray;
            nArray = new int[EndgameChoice.values().length];
            try {
                nArray[EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            $EnumSwitchMapping$3 = nArray;
            nArray = new int[DrawerDestination.values().length];
            try {
                nArray[DrawerDestination.EXECUTIVE_BOARDROOM.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                nArray[DrawerDestination.STOCK_MARKET.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            $EnumSwitchMapping$4 = nArray;
        }
    }
}
