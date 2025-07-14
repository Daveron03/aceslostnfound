/**
 * Data layer for Campus Lost & Found Portal
 * Spring Boot REST API implementation with localStorage fallback
 */

import { Item } from './types';
import { onItemsFallback, addItemFallback, markFoundFallback } from './fallback';

const API_BASE_URL = 'http://localhost:8080/api/items';

/**
 * Fetch wrapper with error handling
 */
async function apiRequest<T>(
  endpoint: string, 
  options: RequestInit = {}
): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;
  
  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`API request failed for ${url}:`, error);
    throw error;
  }
}

/**
 * Convert API response to frontend Item format
 */
function convertFromApiResponse(apiItem: any): Item {
  return {
    id: apiItem.id,
    title: apiItem.title,
    description: apiItem.description,
    photo: apiItem.photoUrl || undefined,
    status: apiItem.status.toLowerCase() as 'lost' | 'found',
    createdAt: new Date(apiItem.createdAt)
  };
}

/**
 * Real-time subscription to all items (polling implementation)
 * @param callback Function called with updated items array
 * @returns Unsubscribe function
 */
export function onItems(callback: (items: Item[]) => void): () => void {
  console.log('🚀 Setting up API polling...');
  
  let isActive = true;
  
  // Initial fetch
  fetchAllItems();
  
  // Poll every 3 seconds
  const intervalId = setInterval(fetchAllItems, 3000);
  
  async function fetchAllItems() {
    if (!isActive) return;
    
    try {
      const apiItems = await apiRequest<any[]>('');
      const items = apiItems.map(convertFromApiResponse);
      console.log('📥 API response received, items:', items.length);
      callback(items);
    } catch (error) {
      console.error('❌ API fetch failed:', error);
      console.warn('⚠️ Falling back to localStorage...');
      return onItemsFallback(callback);
    }
  }
  
  // Return unsubscribe function
  return () => {
    console.log('🛑 Stopping API polling');
    isActive = false;
    clearInterval(intervalId);
  };
}

/**
 * Add a new lost item
 * @param data Item data without id, status, and createdAt
 */
export async function addItem(data: Omit<Item, 'id' | 'status' | 'createdAt'>): Promise<void> {
  console.log('💾 Adding item via API:', data);
  
  try {
    const requestBody = {
      title: data.title,
      description: data.description,
      photoUrl: data.photo || null
    };
    
    await apiRequest('', {
      method: 'POST',
      body: JSON.stringify(requestBody)
    });
    
    console.log('✅ Item added successfully');
  } catch (error) {
    console.error('❌ Error adding item via API:', error);
    console.warn('⚠️ Falling back to localStorage...');
    return addItemFallback(data);
  }
}

/**
 * Mark an item as found
 * @param id Item ID
 */
export async function markFound(id: string): Promise<void> {
  console.log('📍 Marking item as found via API:', id);
  
  try {
    await apiRequest(`/${id}/found`, {
      method: 'PUT'
    });
    
    console.log('✅ Item marked as found');
  } catch (error) {
    console.error('❌ Error marking item as found via API:', error);
    console.warn('⚠️ Falling back to localStorage...');
    return markFoundFallback(id);
  }
}
