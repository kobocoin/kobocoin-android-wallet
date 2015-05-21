/*
 * Copyright 2012-2014 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bushstar.kobocoin_android_wallet.service;

import java.util.List;

import javax.annotation.CheckForNull;

import com.bushstar.kobocoinj.core.Peer;
import com.bushstar.kobocoinj.core.StoredBlock;

/**
 * @author Andreas Schildbach
 */
public interface BlockchainService
{
	String ACTION_PEER_STATE = BlockchainService.class.getPackage().getName() + ".peer_state";
	String ACTION_PEER_STATE_NUM_PEERS = "num_peers";

	String ACTION_BLOCKCHAIN_STATE = BlockchainService.class.getPackage().getName() + ".blockchain_state";
	String ACTION_BLOCKCHAIN_STATE_BEST_CHAIN_DATE = "best_chain_date";
	String ACTION_BLOCKCHAIN_STATE_BEST_CHAIN_HEIGHT = "best_chain_height";
	String ACTION_BLOCKCHAIN_STATE_REPLAYING = "replaying";
	String ACTION_BLOCKCHAIN_STATE_DOWNLOAD = "download";
	int ACTION_BLOCKCHAIN_STATE_DOWNLOAD_OK = 0;
	int ACTION_BLOCKCHAIN_STATE_DOWNLOAD_STORAGE_PROBLEM = 1;
	int ACTION_BLOCKCHAIN_STATE_DOWNLOAD_NETWORK_PROBLEM = 2;

	String ACTION_CANCEL_COINS_RECEIVED = BlockchainService.class.getPackage().getName() + ".cancel_coins_received";
	String ACTION_RESET_BLOCKCHAIN = BlockchainService.class.getPackage().getName() + ".reset_blockchain";
	String ACTION_BROADCAST_TRANSACTION = BlockchainService.class.getPackage().getName() + ".broadcast_transaction";
	String ACTION_BROADCAST_TRANSACTION_HASH = "hash";

	@CheckForNull
	List<Peer> getConnectedPeers();

	List<StoredBlock> getRecentBlocks(int maxBlocks);
}
